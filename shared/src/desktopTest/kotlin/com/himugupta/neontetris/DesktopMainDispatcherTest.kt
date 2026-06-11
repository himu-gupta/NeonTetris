package com.himugupta.neontetris

import java.awt.EventQueue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopMainDispatcherTest {
  @Test
  fun mainDispatcherRunsOnSwingEventThread() = runBlocking {
    withContext(Dispatchers.Main) {
      assertTrue(EventQueue.isDispatchThread())
    }
  }
}
