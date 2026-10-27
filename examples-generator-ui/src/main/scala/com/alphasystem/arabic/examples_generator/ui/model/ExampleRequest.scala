package com.alphasystem
package arabic
package examples_generator
package ui
package model

import examples_generator.model.ExampleData

import java.nio.file.Path

case class ExampleRequest(path: Path, exampleData: ExampleData)
