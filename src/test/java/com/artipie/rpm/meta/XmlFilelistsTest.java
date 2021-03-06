/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.rpm.meta;

import com.jcabi.matchers.XhtmlMatchers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link XmlFilelists}.
 *
 * @since 0.6.3
 */
public final class XmlFilelistsTest {

    @Test
    public void writesFile(@TempDir final Path temp) throws Exception {
        final Path file = temp.resolve("filelists.xml");
        try (XmlFilelists list = new XmlFilelists(file)) {
            list.startPackages()
                .startPackage("packagename", "packagearch", "packagechecksun")
                .version(1, "packageversion", "packagerel")
                .files(
                    new String[] {"file" },
                    new String[] {"dir" },
                    new int[] {0 }
                ).close();
        }
        MatcherAssert.assertThat(
            new String(Files.readAllBytes(file), StandardCharsets.UTF_8),
            XhtmlMatchers.hasXPaths(
                // @checkstyle LineLengthCheck (2 line)
                "/*[local-name()='filelists']/*[local-name()='package' and @pkgid='packagechecksun' and @name='packagename' and @arch='packagearch']/*[local-name()='version' and @epoch='1' and @ver='packageversion' and @rel='packagerel']",
                "/*[local-name()='filelists']/*[local-name()='package' and @pkgid='packagechecksun' and @name='packagename' and @arch='packagearch']/*[local-name()='file' and text()='dirfile']"
            )
        );
    }
}
