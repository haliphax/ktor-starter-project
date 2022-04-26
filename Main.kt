package com.haliphax.ktest

import com.jcraft.jsch.*
import java.util.Base64
import kotlin.system.exitProcess

fun main() {
	val jsch = JSch()

	print("Host: ")
	val split = readLine()?.split(':')
	val host = split?.get(0)
	val port = split?.get(1)?.toInt() ?: 22

	print("Host key: ")
	val rawKey = readLine()
	val bytes = Base64.getDecoder().decode(rawKey)
	val key = HostKey(host, bytes)
	jsch.hostKeyRepository.add(key, null)

	print("User: ")
	val user = readLine()
	val sess = jsch.getSession(user, host, port)

	print("Password: ")
	val pwd = readLine()
	sess.setPassword(pwd)

	try {
		sess.connect(30000)
	}
	catch (ex: JSchException) {
		println(ex.localizedMessage)
		exitProcess(1)
	}

	val chan = sess.openChannel("shell")
	chan.setInputStream(System.`in`)
	chan.setOutputStream(System.out)

	try {
		chan.connect(3000)
	}
	catch (ex: JSchException) {
		println(ex.localizedMessage)
		exitProcess(1)
	}
}
