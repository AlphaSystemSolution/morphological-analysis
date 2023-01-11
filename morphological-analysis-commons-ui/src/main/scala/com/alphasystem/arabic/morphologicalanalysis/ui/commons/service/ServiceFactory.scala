package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package commons
package service

import morphology.graph.model.{ DependencyGraph, GraphNode, RelationshipNode }
import morphology.model.{ Chapter, Location, Token, Verse }
import morphology.persistence.cache.*
import javafx.concurrent
import javafx.concurrent.{ Task, Service as JService }
import scalafx.concurrent.Service

import java.util.UUID

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

  lazy val createNodeService: CreateNodeRequest => Service[Unit] =
    (request: CreateNodeRequest) =>
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] =
          new Task[Unit]():
            override def call(): Unit = {
              database.createNode(request.node)
              val dependencyGraph = request.dependencyGraph
              dependencyGraph.verseNumbers.foreach { verseNumber =>
                cacheFactory
                  .dependencyGraphByChapterAndVerseNumber
                  .invalidate(GetDependencyGraphRequest(dependencyGraph.chapterNumber, verseNumber))
              }
            }
      }) {}

  lazy val removeNodeService: RemoveNodeByIdRequest => Service[Unit] =
    (request: RemoveNodeByIdRequest) =>
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] =
          new Task[Unit]():
            override def call(): Unit = {
              database.removeNode(request.nodeId)
              val dependencyGraph = request.dependencyGraph
              dependencyGraph.verseNumbers.foreach { verseNumber =>
                cacheFactory
                  .dependencyGraphByChapterAndVerseNumber
                  .invalidate(GetDependencyGraphRequest(dependencyGraph.chapterNumber, verseNumber))
              }
            }
      }) {}

  lazy val getDependencyGraphByIdService: UUID => Service[Option[DependencyGraph]] =
    (id: UUID) =>
      new Service[Option[DependencyGraph]](new JService[Option[DependencyGraph]] {
        override def createTask(): Task[Option[DependencyGraph]] =
          new Task[Option[DependencyGraph]]():
            override def call(): Option[DependencyGraph] = cacheFactory.dependencyGraphById.get(id)
      }) {}

  lazy val getDependencyGraphByChapterAndVerseNumberService
    : GetDependencyGraphRequest => Service[Seq[DependencyGraph]] =
    (request: GetDependencyGraphRequest) =>
      new Service[Seq[DependencyGraph]](new JService[Seq[DependencyGraph]] {
        override def createTask(): Task[Seq[DependencyGraph]] =
          new Task[Seq[DependencyGraph]]():
            override def call(): Seq[DependencyGraph] =
              cacheFactory.dependencyGraphByChapterAndVerseNumber.get(request)
      }) {}

  lazy val getGraphNodeService: UUID => Service[Option[GraphNode]] =
    (id: UUID) =>
      new Service[Option[GraphNode]](new JService[Option[GraphNode]] {
        override def createTask(): Task[Option[GraphNode]] =
          new Task[Option[GraphNode]]():
            override def call(): Option[GraphNode] = cacheFactory.database.findGraphNodeById(id)
      }) {}
}

object ServiceFactory {

  case class SaveDependencyGraphRequest(dependencyGraph: DependencyGraph, recreate: Boolean = false)

  case class CreateNodeRequest(dependencyGraph: DependencyGraph, node: GraphNode)

  case class RemoveNodeByIdRequest(dependencyGraph: DependencyGraph, nodeId: UUID)

  def apply(cacheFactory: CacheFactory): ServiceFactory = new ServiceFactory(cacheFactory)
}
