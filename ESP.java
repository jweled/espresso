
/**
 * Espresso Server Page parsing class
 * 
 * @author jweled
 * @version 1.0.0
 */
import java.util.*;
import javax.script.*;
import java.io.*;
public class ESP {
    private static List recIndex(String str, String match, int offset) {
        int sti = str.indexOf(match);
        List<Integer> starts = new ArrayList<Integer>();
        while (sti >= 0) {
            starts.add(sti + offset);
            sti = str.indexOf(match, sti + 1);
        }
        return starts;
    }
    public static String parse(String file, boolean verbose) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        ScriptContext ctx = new SimpleScriptContext();
        Bindings gb = engine.createBindings();
        file = file.replace(System.lineSeparator(), "");
        try {
            StringWriter writer = new StringWriter();
            ctx.setWriter(writer);
            ctx.setBindings(gb, ScriptContext.GLOBAL_SCOPE);
            List<Integer> st = recIndex(file, "[[ESP", 5);
            List<Integer> end = recIndex(file, "END]]", 0);
            int offset = 0;
            for (int i=0; i < end.size(); i++) {
                String block = file.substring(st.get(i) - offset, end.get(i) - offset);
                if (verbose) {
                    main.print("[PARSE BLOCK] " + block + " [---]");
                }
                writer.getBuffer().setLength(0);
                try {
                    Object res = engine.eval(block, ctx);
                } catch (Exception z) {
                    try {
                        writer.append("<br><font color='red'>[ERR]:</font> " + z.getMessage());
                    } catch (Exception f) {
                        main.print("[ERR]: " + z.getStackTrace());
                    }
                }
                file = file.substring(0,
                    st.get(i) - offset - 5)
                    + writer.toString().replace("\n", "<br>")
                    + file.substring(end.get(i) - offset + 5,
                    file.length());
                offset += block.length() - writer.toString().length() + 10;
            }
            if (verbose) {
                main.print("[PARSED FILE] " + file);
            }
        } catch (Exception e) {
            main.print("[ERR] Failed to initialize");
            e.printStackTrace();
        }
        return file;
    }
}