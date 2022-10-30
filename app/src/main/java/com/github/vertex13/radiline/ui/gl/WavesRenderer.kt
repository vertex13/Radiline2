package com.github.vertex13.radiline.ui.gl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.github.vertex13.radiline.R
import com.github.vertex13.radiline.logger.logE
import com.github.vertex13.radiline.system.AppContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val V_POSITION_ID = 0
private const val V_DIMENSIONS = 2
private const val V_TYPE_SIZE = 4
private const val I_TYPE_SIZE = 2
private const val V_STRIDE = V_DIMENSIONS * V_TYPE_SIZE
private val V_POSITIONS = floatArrayOf(-1f, -1f, -1f, 1f, 1f, 1f, 1f, -1f)
private val V_INDICES = shortArrayOf(0, 1, 2, 0, 2, 3)

class WavesRenderer(
    private val appContext: AppContext,
    private val params: Params,
) : GLSurfaceView.Renderer {
    class Params(val bgColor: GLVector)

    private val vPositionsBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(V_POSITIONS.size * V_TYPE_SIZE).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(V_POSITIONS)
                position(0)
            }
        }
    private val vIndicesBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(V_INDICES.size * I_TYPE_SIZE).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(V_INDICES)
                position(0)
            }
        }
    private var shaderProgram: Int = 0
    private var initTime: Long = 0L
    private var isError: Boolean = false

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        val res = appContext.value.resources
        val vShaderCode =
            res.openRawResource(R.raw.common_vertex_shader).bufferedReader().use { it.readText() }
        val fShaderCode =
            res.openRawResource(R.raw.waves_fragment_shader).bufferedReader().use { it.readText() }
        try {
            shaderProgram = createShaderProgram(vShaderCode, fShaderCode)
        } catch (e: Exception) {
            logE("Create shader program error.", e)
            isError = true
        }
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        if (isError) {
            return
        }
        GLES20.glVertexAttribPointer(
            V_POSITION_ID, V_DIMENSIONS, GLES20.GL_FLOAT, false, V_STRIDE, vPositionsBuffer
        )
        GLES20.glEnableVertexAttribArray(V_POSITION_ID)
        GLES20.glUseProgram(shaderProgram)

        val uBackground = GLES20.glGetUniformLocation(shaderProgram, "uBackground")
        GLES20.glUniform4fv(uBackground, 1, params.bgColor.array, 0)

        initTime = System.currentTimeMillis()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        if (isError) {
            return
        }
        GLES20.glUseProgram(shaderProgram)

        val uResolution = GLES20.glGetUniformLocation(shaderProgram, "uResolution")
        GLES20.glUniform2f(uResolution, width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        if (isError) {
            return
        }
        GLES20.glUseProgram(shaderProgram)

        val time = (System.currentTimeMillis() - initTime) / 1000f
        val uTime = GLES20.glGetUniformLocation(shaderProgram, "uTime")
        GLES20.glUniform1f(uTime, time)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, V_INDICES.size, GLES20.GL_UNSIGNED_SHORT, vIndicesBuffer
        )
    }
}
