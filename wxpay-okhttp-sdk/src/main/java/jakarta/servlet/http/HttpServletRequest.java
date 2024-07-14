package jakarta.servlet.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lqb
 * @since 2024/7/14 16:31
 **/
public interface HttpServletRequest {

    String getHeader(String name);

    InputStream getInputStream() throws IOException;
}
