package org.eclipse.lmos.starter.template.engine


import com.github.mustachejava.DefaultMustacheFactory
import java.io.Reader
import java.io.StringWriter

interface TemplateEngine {
    /**  
     * Renders a template with the provided model data.  
     *  
     * @param templateName The name or path of the template file.  
     * @param model A map containing the data to populate the template.  
     * @return The rendered template as a String.  
     */  
    fun render(reader: Reader, templateName: String, model: Map<String, Any>): String
}

class MustacheTemplateEngine : TemplateEngine {
    private val mustacheFactory = DefaultMustacheFactory()

    override fun render(reader: Reader, templateName: String, model: Map<String, Any>): String {
        val mustache = mustacheFactory.compile(reader, templateName)
        val writer = StringWriter()
        mustache.execute(writer, model).flush()
        return writer.toString()
    }
}