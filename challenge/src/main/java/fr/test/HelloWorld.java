package fr.test;

import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import io.javalin.rendering.template.TemplateUtil;
//import io.javalin.http.Handler;
import io.javalin.rendering.template.JavalinPebble;
import io.javalin.rendering.JavalinRenderer;

import de.neuland.pug4j.Pug4J;
import de.neuland.pug4j.exceptions.PugLexerException;
import de.neuland.pug4j.exceptions.PugParserException;
import de.neuland.pug4j.template.FileTemplateLoader;
import de.neuland.pug4j.template.PugTemplate;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;

public class HelloWorld {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.routing.treatMultipleSlashesAsSingleSlash = true;
            config.routing.caseInsensitiveRoutes = true;
        })
            .get("/", ctx -> ctx.result("Hello World"))
            .start(7070);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
        }));

        JavalinRenderer.register((filePath, model, ctx) -> {
            PugTemplate template = Pug4J.getTemplate(filePath);
            return Pug4J.render(template, model);
        }, ".pug");

        app.get("/pug", ctx -> {
            ctx.render("templates/test.pug", TemplateUtil.model("firstName", "John", "lastName", "Doe"));
        });
    }
}