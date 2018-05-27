public class Rectangle {
	public int length;
	public int width;
	public int nothing;

	public Rectangle(int length, int width){
		this.length = length;
		this.width = width;
	}

	public int getArea () {
		return length * width;
	}

	public int getPerimeter (){
		return 2* (length + width);
	}
}
