package 客户端与服务端;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class User
{
	int number = -1;
	int LeftOrRight = 0;
	boolean isReady;
	String name = "          ";
	String imagePath = null;
	DataInputStream dataInput = null;
	DataOutputStream dataOutput = null;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getImagePath()
	{
		return imagePath;
	}
	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}
	public DataInputStream getDataInput()
	{
		return dataInput;
	}
	public void setDataInput(DataInputStream dataInput)
	{
		this.dataInput = dataInput;
	}
	public DataOutputStream getDataOutput()
	{
		return dataOutput;
	}
	public void setDataOutput(DataOutputStream dataOutput)
	{
		this.dataOutput = dataOutput;
	}
	
	public User() {}
	
	public User(String name, String imagePath, DataInputStream dataInput, DataOutputStream dataOutput)
	{
		this.name = name;
		this.imagePath = imagePath;
		this.dataInput = dataInput;
		this.dataOutput = dataOutput;
	}
	
}
