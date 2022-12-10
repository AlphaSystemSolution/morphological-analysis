package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package commons
package service

import morphology.graph.model.DependencyGraph
import morphology.model.{ Chapter, Location, Token, Verse }
import morphology.persistence.cache.*
import javafx.concurrent
import javafx.concurrent.{ Task, Service as JService }
import scalafx.concurrent.Service

class ServiceFactory(cacheFactory: CacheFactory) {

  import ServiceFactory.*

  private lazy val database = cacheFactory.database

  lazy val chapterService: Service[Seq[Chapter]] =
    new Service[Seq[Chapter]](
      new JService[Seq[Chapter]]:
        override def createTask(): Task[Seq[Chapter]] =
          new Task[Seq[Chapter]]():
            override def call(): Seq[Chapter] = cacheFactory.database.findAllChapters
    ) {}

  lazy val verseService: Int => Service[Seq[Verse]] =
    (chapterNumber: Int) =>
      new Service[Seq[Verse]](
        new JService[Seq[Verse]]:
          override def createTask(): Task[Seq[Verse]] =
            new Task[Seq[Verse]]():
              override def call(): Seq[Verse] = cacheFactory.verses.get(chapterNumber)
      ) {}

  lazy val tokenService: TokenRequest => Service[Seq[Token]] =
    (tokenRequest: TokenRequest) =>
      new Service[Seq[Token]](
        new JService[Seq[Token]]:
          override def createTask(): Task[Seq[Token]] =
            new Task[Seq[Token]]():
              override def call(): Seq[Token] = cacheFactory.tokens.get(tokenRequest)
      ) {}

  lazy val saveData: Token => Service[Unit] =
    (token: Token) =>
      new Service[Unit](
        new JService[Unit] {
          override def createTask(): Task[Unit] = {
            new Task[Unit]() {
              override def call(): Unit = {
                database.updateToken(token)
                cacheFactory.tokens.invalidate(TokenRequest(token.chapterNumber, token.verseNumber))
              }
            }
          }
        }
      ) {}

  lazy val recreateTokens: Seq[Token] => Service[Unit] =
    (tokens: Seq[Token]) =>
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] = {
          new Task[Unit]():
            override def call(): Unit = {
              val token = tokens.head
              val tokenRequest = TokenRequest(token.chapterNumber, token.verseNumber)
              database.removeTokensByVerseId(tokenRequest.verseId)
              database.createTokens(tokens)
              cacheFactory.tokens.invalidate(tokenRequest)
            }
        }
      }) {}

  lazy val createDependencyGraphService: SaveDependencyGraphRequest => Service[Unit] =
    (request: SaveDependencyGraphRequest) =>
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] = {
          new Task[Unit]() {
            override def call(): Unit = {
              val dependencyGraph = request.dependencyGraph
              if request.recreate then database.removeNodesByDependencyGraphId(dependencyGraph.id)

              database.createOrUpdateDependencyGraph(dependencyGraph)
              dependencyGraph.verseNumbers.foreach { verseNumber =>
                cacheFactory
                  .dependencyGraphByChapterAndVerseNumber
                  .invalidate(GetDependencyGraphRequest(dependencyGraph.chapterNumber, verseNumber))
              }

            }
          }
        }
      }) {}

  /*lazy val updateDependencyGraphService: SaveDependencyGraphRequest => Service[Unit] =
    (request: SaveDependencyGraphRequest) =>
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] = {
          new Task[Unit]() {
            override def call(): Unit = {
              val dependencyGraph = request.dependencyGraph
              dependencyGraphRepository.update(dependencyGraph)
              cacheFactory.dependencyGraph.put(dependencyGraph.id, Some(dependencyGraph))
              graphNodeRepository.createAll(request.nodes)
            }
          }
        }
      }) {}*/

  /*lazy val getDependencyGraphByIdService: String => Service[Option[DependencyGraph]] =
    (id: String) =>
      new Service[Option[DependencyGraph]](new JService[Option[DependencyGraph]] {
        override def createTask(): Task[Option[DependencyGraph]] =
          new Task[Option[DependencyGraph]]():
            override def call(): Option[DependencyGraph] = cacheFactory.dependencyGraph.get(id)
      }) {}*/

  lazy val getDependencyGraphByChapterAndVerseNumberService
    : GetDependencyGraphRequest => Service[Seq[DependencyGraph]] =
    (request: GetDependencyGraphRequest) =>
      new Service[Seq[DependencyGraph]](new JService[Seq[DependencyGraph]] {
        override def createTask(): Task[Seq[DependencyGraph]] =
          new Task[Seq[DependencyGraph]]():
            override def call(): Seq[DependencyGraph] =
              cacheFactory.dependencyGraphByChapterAndVerseNumber.get(request)
      }) {}

  /*lazy val getGraphNodesService: String => Service[List[GraphNode]] =
    (graphId: String) =>
      new Service[List[GraphNode]](new JService[List[GraphNode]] {
        override def createTask(): Task[List[GraphNode]] =
          new Task[List[GraphNode]]():
            override def call(): List[GraphNode] = cacheFactory.graphNodes.get(graphId)
      }) {}*/

}

object ServiceFactory {

  case class SaveDependencyGraphRequest(dependencyGraph: DependencyGraph, recreate: Boolean = false)

  def apply(cacheFactory: CacheFactory): ServiceFactory = new ServiceFactory(cacheFactory)
}
