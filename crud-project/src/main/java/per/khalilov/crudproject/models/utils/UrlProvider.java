package per.khalilov.crudproject.models.utils;

import com.ibm.icu.text.Transliterator;

public class UrlProvider {
    private static final String TRANSLITERATION_TYPE = "Russian-Latin/BGN";

    public static String getUrlFromName(String name) {
        Transliterator transliterator = Transliterator.getInstance(TRANSLITERATION_TYPE);
        String result = transliterator.transliterate(name);
        return result.replaceAll(" ", "_");
    }
}
