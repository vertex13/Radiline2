package com.github.vertex13.radiline.ui.gl

import android.opengl.GLES20

fun createShaderProgram(vertexShaderCode: String, fragmentShaderCode: String): Int {
    val status = IntArray(1)

    val vsId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
    if (vsId == 0) {
        error("Create vertex shader error.")
    }
    GLES20.glShaderSource(vsId, vertexShaderCode)
    GLES20.glCompileShader(vsId)
    GLES20.glGetShaderiv(vsId, GLES20.GL_COMPILE_STATUS, status, 0)
    if (status.first() == GLES20.GL_FALSE) {
        val log = GLES20.glGetShaderInfoLog(vsId)
        GLES20.glDeleteShader(vsId)
        error("Vertex shader compilation error.\n$log")
    }

    val fsId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
    if (fsId == 0) {
        error("Create fragment shader error.")
    }
    GLES20.glShaderSource(fsId, fragmentShaderCode)
    GLES20.glCompileShader(fsId)
    GLES20.glGetShaderiv(fsId, GLES20.GL_COMPILE_STATUS, status, 0)
    if (status.first() == GLES20.GL_FALSE) {
        val log = GLES20.glGetShaderInfoLog(fsId)
        GLES20.glDeleteShader(vsId)
        GLES20.glDeleteShader(fsId)
        error("Fragment shader compilation error.\n$log")
    }

    val programId = GLES20.glCreateProgram()
    if (programId == 0) {
        error("Create program error.")
    }
    GLES20.glAttachShader(programId, vsId)
    GLES20.glAttachShader(programId, fsId)

    GLES20.glLinkProgram(programId)
    GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, status, 0)
    if (status.first() == GLES20.GL_FALSE) {
        val log = GLES20.glGetProgramInfoLog(programId)
        GLES20.glDeleteProgram(programId)
        GLES20.glDeleteShader(vsId)
        GLES20.glDeleteShader(fsId)
        error("Program linking error.\n$log")
    }

    GLES20.glValidateProgram(programId)
    GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, status, 0)
    if (status.first() == GLES20.GL_FALSE) {
        val log = GLES20.glGetProgramInfoLog(programId)
        GLES20.glDeleteProgram(programId)
        GLES20.glDeleteShader(vsId)
        GLES20.glDeleteShader(fsId)
        error("Program validation error.\n$log")
    }
    GLES20.glDetachShader(programId, vsId)
    GLES20.glDetachShader(programId, fsId)
    return programId
}
