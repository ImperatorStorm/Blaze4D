package me.hydos.rosella.render.material

import me.hydos.rosella.Rosella
import me.hydos.rosella.render.Topology
import me.hydos.rosella.render.resource.Identifier
import me.hydos.rosella.render.resource.Resource
import me.hydos.rosella.render.shader.ShaderProgram
import me.hydos.rosella.render.texture.SamplerCreateInfo
import me.hydos.rosella.render.texture.StbiImage
import me.hydos.rosella.render.texture.Texture
import me.hydos.rosella.render.vertex.VertexFormat

/**
 * A Material is like texture information, normal information, and all of those things which give an object character wrapped into one class.
 * similar to how unity material's works
 * guaranteed to change in the future
 */
open class Material(
	val resource: Resource,
	val shaderId: Identifier,
	val imgFormat: Int,
	val useBlend: Boolean,
	val topology: Topology,
	val vertexFormat: VertexFormat,
	val samplerCreateInfo: SamplerCreateInfo
) {
	lateinit var pipeline: PipelineInfo

	lateinit var shader: ShaderProgram

	lateinit var texture: Texture

	open fun loadShaders(engine: Rosella) {
		val retrievedShader = engine.shaderManager.getOrCreateShader(shaderId)
			?: error("The shader $shaderId couldn't be found. (Are you registering it?)")
		this.shader = retrievedShader
	}

	open fun loadTextures(engine: Rosella) {
		if (resource != Resource.Empty) {
			val test = engine.textureManager.generateTextureId() // FIXME this is temporary
			engine.textureManager.uploadTextureToId(engine, test, StbiImage(resource), 0, 0, imgFormat, samplerCreateInfo)
			texture = engine.textureManager.getTexture(test)!!
		}
	}
}
