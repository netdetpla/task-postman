package org.ndp.task_postman

import org.jboss.arquillian.junit.Arquillian
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(Arquillian::class)
class MainKtTest {

    @Test
    fun postIPTestTask() {
        postIPTestTask(
                "http://192.168.1.120:8080/task",
                "ip-test",
                "1.0",
                "test",
                "5",
                "114.114.114.1/28"
        )
    }
}
