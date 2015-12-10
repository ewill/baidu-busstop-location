package com.yourblogz.busstop.busline;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>通过配置文件加载路线名称</p>
 * <ul>
 *     <li>通过目录路径加载：/path/directory</li>
 *     <li>通过文件路径加载：/path/directory/filename</li>
 *     <li>通过类的路径加载：classpath:filename</li>
 * </ul>
 * <p>文件内容以行为单位，请参见dongguan.bl文件的格式</p>
 */
public abstract class FileBuslineGenerator extends BuslineGenerator {
    
    private static final String CONFIG_FILE_EXT = ".bl";
    private static final String CLASSPATH = "classpath:";
    private static final Logger log = LoggerFactory.getLogger(FileBuslineGenerator.class);

    public FileBuslineGenerator(int cityCode, String filePath) {
        super(cityCode, loadConfigure(filePath));
    }
    
    private static final String[] loadConfigure(String filePath) {
        final List<String> buslines = new ArrayList<>();
        Path path = Paths.get(filePath.startsWith(CLASSPATH) ? ClassLoader.getSystemResource(filePath.replaceAll(CLASSPATH, "")).getPath().substring(1) : filePath);
        
        try {
            if (path.toFile().isDirectory()) {
                Files.walkFileTree(path,  EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>(){
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(CONFIG_FILE_EXT)) {
                            loadFile(buslines, file);
                            log.info("Readed file: " + file.toAbsolutePath());
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else if (path.toFile().isFile() && path.toString().endsWith(CONFIG_FILE_EXT)) {
                loadFile(buslines, path);
                log.info("Readed file: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        
        log.info(buslines.toString());
        return buslines.toArray(new String[0]);
    }

    private static void loadFile(List<String> buslines, Path file) throws IOException {
        buslines.addAll(Files.readAllLines(file, Charset.forName("UTF-8")));
    }

}
