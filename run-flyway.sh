#!/bin/sh

sbt -Dpostgres_user=morphological_analysis -Dpassword=postgres flywayMigrate
