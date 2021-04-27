package world;

import java.io.File;

public class Build {
	int horizontal, vertical, high;
	File file;
	public Build(int h, int v, int hi, File f) {
		horizontal = h;
		vertical = v;
		high = hi;
		file = f;
	}
	
	public File getFile() {
		return file;
	}
	public int getHorizontal() {
		return horizontal;
	}
	public int getVertical() {
		return vertical;
	}
	public int getHigh() {
		return high;
	}
}
