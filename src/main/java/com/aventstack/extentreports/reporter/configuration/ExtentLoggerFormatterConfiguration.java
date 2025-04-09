package com.aventstack.extentreports.reporter.configuration;

import java.io.File;
import java.util.stream.Stream;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.AbstractReporter;
import com.aventstack.extentreports.reporter.ExtentLoggerReporter;
import com.aventstack.extentreports.resource.OfflineResxDelegate;
import com.aventstack.extentreports.utils.FileUtil;

/**
 * Defines configuration settings for the HTML reporter
 */
public class ExtentLoggerFormatterConfiguration 
	extends BasicFileConfiguration 
	implements IReporterConfiguration {

    private Protocol protocol;
    private Theme theme;
    
    public ExtentLoggerFormatterConfiguration(AbstractReporter reporter) {
    	super(reporter);
    }
    
    /**
     * Sets the protocol of accessing CSS/JS resources from CDN
     * 
     * <p>
     * Default protocol value: HTTPS
     * </p>
     * 
     * @param protocol Protocol, HTTPS or HTTP
     */
    public void setProtocol(Protocol protocol) {
        usedConfigs.put("protocol", String.valueOf(protocol).toLowerCase());
        this.protocol = protocol; 
    }
    
    public Protocol getProtocol() {
    	return protocol; 
	}
    
    /**
     * Sets the {@link Theme} of the report
     * 
     * @param theme {@link Theme}
     */
    public void setTheme(Theme theme) {
        usedConfigs.put("theme", String.valueOf(theme).toLowerCase());
        this.theme = theme; 
    }
    
    public Theme getTheme() { 
    	return theme; 
	}
    
    /**
     * Setting to automatically store screen shots relative to the path. This method also sets the new relative
     * path as a link from the report. Example:
     * 
     * <pre>
     * /
     *   Report.html
     *   Report.0
     *     - 1.png
     *     - 2.png
     *   Report.1
     *     - 1.png
     *     - 2.png
     * </pre>
     * 
     * <p>
     * Report.0 directory will contain media from the 1st run. 
     * Report.1 directory will contain media from the 2nd run.
     * </p>
     * 
     * @param v Setting to enable this feature
     */
    public void setAutoCreateRelativePathMedia(boolean v) {
    	usedConfigs.put("autoCreateRelativePathMedia", String.valueOf(v));
    }

    /**
     * Creates the HTML report, saving all resources (css, js, fonts) in the same location, so the
     * report can be viewed without an internet connection
     * 
     * @param offline Setting to enable an offline accessible report
     */
    public void enableOfflineMode(Boolean offline) {
    	usedConfigs.put("enableOfflineMode", String.valueOf(offline));
    	usedConfigs.put("offlineDirectory", getReporter().getReporterName() + "/");
    	if (offline) {
    		File f = getTargetDirectory(((ExtentLoggerReporter)getReporter()).getFileFile());
    		String s = "/";
    		String resourcePackagePath = ExtentReports.class.getPackage().getName().replace(".", s);
            resourcePackagePath += s + "offline" + s;
    		String[] resx = combine(getJSFiles(), 
    				getCSSFiles(),
    				getIconFiles(), 
    				getImgFiles());
    		OfflineResxDelegate.saveOfflineResources(resourcePackagePath, resx, f.getAbsolutePath());
    	}
    }

    private File getTargetDirectory(File f) {
    	String dir;
    	if (FileUtil.isDirectory(f)) {
    		dir = f.getAbsolutePath().replace("\\", "/");
        } else {
        	dir = f.getAbsolutePath().replace("\\", "/");
        	dir = new File(dir).getParent();
        }
    	dir += "/" + getReporter().getReporterName();
    	return new File(dir);
    }
    
    private String[] combine(String[]... array) {
    	String[] result = new String[] {};
    	for (String[] arr : array) {
    		result = Stream.of(result, arr).flatMap(Stream::of).toArray(String[]::new);
    	}
    	return result;
    }

    private String[] getJSFiles() {
    	String commonsPath = "commons/js/";
        String reporterPath = getReporter().getReporterName() + "/js/";
        String[] files = { 
        		commonsPath + "attr.js",
        		commonsPath + "dashboard.js",
        		reporterPath + "logger-scripts.js"
        };
        return files;
    }
    
    private String[] getCSSFiles() {
    	String stylesPath = "css/";
    	String reporterPath = getReporter().getReporterName() + "/" + stylesPath + "/";
        String[] files = { 
        		reporterPath + "logger-style.css", 
        };
        return files;
    }
    
    private String[] getIconFiles() {
        String path = "commons/css/icons/";
        String iconDirPath = "fontawesome/";
        String[] files = {
                path + "font-awesome.min.css",
                path + iconDirPath + "fontawesome-webfont.eot",
                path + iconDirPath + "fontawesome-webfont.svg",
                path + iconDirPath + "fontawesome-webfont.ttf",
                path + iconDirPath + "fontawesome-webfont.woff",
                path + iconDirPath + "fontawesome-webfont.woff2",
                path + iconDirPath + "FontAwesome.otf",
        };
        return files;
    }

    private String[] getImgFiles() {
        String path = "commons/img/";
        String[] files = {
                path + "logo.png"
        };
        return files;
    }
    
}
