//Name: Harish Pumbhadia
//Student ID: 1001773121

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class localClientHandler extends Thread{
	File src,dest;
	Path source;
	final String identifier;
	String[] localDirectory;
	localClientHandler(String identifier, String[] localDirectory){
		this.identifier = identifier;
		this.localDirectory = localDirectory;
	}
	public localClientHandler(String identifier) {
		//this.localDirectory = null;
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
	}
	public void run() {
		copy();
	

	
	try(WatchService service = FileSystems.getDefault().newWatchService()) {
		Map<WatchKey, Path> keyMap = new HashMap<>();
		keyMap.put(source.register(service, 
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY), source);
		WatchKey watchKey;
		
		do {
			watchKey = service.take();
			Path eventDir = keyMap.get(watchKey);
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				Path eventPath = (Path)event.context();
				deleteCopy();
				copy();
			}
		}while(watchKey.reset());
		
//		CustomFileVisitor fileVisitor = new CustomFileVisitor(source, target);
//        //You can specify your own FileVisitOption
//        Files.walkFileTree(source, fileVisitor);
	} catch (Exception e) {
		// TODO Auto-generated catch block
	}
	}
	public void desync() {
		
			File local = new File("D:\\Study\\DS-Lab\\Local Directory\\"+identifier);
			if(local.isDirectory()) {
				try {
					FileUtils.deleteDirectory(local);
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			else {
				System.out.println("Local Directory not found");
			}
		
	}
	public void deleteCopy(){
		if(dest.isDirectory()) {
			try {
				FileUtils.deleteDirectory(dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}
	public void copy() {
		for(String iterator: localDirectory) {
			 src = new File("D:\\Study\\DS-Lab\\lab\\"+iterator);
			 dest = new File("D:\\Study\\DS-Lab\\Local Directory\\"+this.identifier);
			source = Paths.get("D:\\Study\\DS-Lab\\lab\\"+iterator);
		   
		    try {
				FileUtils.copyDirectoryToDirectory(src, dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
					
			}
	}
}