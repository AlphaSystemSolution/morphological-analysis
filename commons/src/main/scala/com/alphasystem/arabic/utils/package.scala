package com.alphasystem
package arabic

package object utils {

  extension (src: String) {
    def asResourceUrl: String = Thread.currentThread().getContextClassLoader.getResource(src).toExternalForm
  }
}
