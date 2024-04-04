package ir.albino.client.features.ui.html.serialize;

import ir.albino.client.features.ui.html.annotations.HTMLIgnore;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@Getter
public class HTMLParser<T> {
    public final T obj;

    public HTMLParser(String path, T obj) throws IOException {
        this(Jsoup.parse(new File(path)), obj);
    }

    public HTMLParser(Element element, T obj) {
        this.obj = obj;
        for (Element e : element.getAllElements()) {
            try {
                if (!e.id().isEmpty() && e.getAllElements().indexOf(e) != 0) {
                    Field f = obj.getClass().getDeclaredField(e.id());
                    if (!f.isAnnotationPresent(HTMLIgnore.class))
                        this.setFieldValue(f, obj, e);
                }
            } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException |
                     InstantiationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void setFieldValue(Field f, Object inst, Element v) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Object fObj = f.get(obj);
        System.out.println(v);
        if (fObj instanceof Integer)
            f.set(inst, Integer.parseInt(v.text()));
        else if (fObj instanceof Boolean) f.set(inst, Boolean.parseBoolean(v.text()));
        else if (fObj instanceof HTMLSerializable) {
            HTMLSerializable.deserialize(v, (Class<? extends HTMLSerializable>) Class.forName(v.attr("type")));
        } else if (fObj instanceof Enum<?>) {
            f.set(inst, Enum.valueOf(((Enum<?>) fObj).getClass(), v.text()));
        } else f.set(inst, v.text());
    }

    public HTMLParser(ResourceLocation res, T obj) throws IOException {
        this(String.format("assets/%s/%s", res.getResourceDomain(), res.getResourcePath()), obj);
    }

}