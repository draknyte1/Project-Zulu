package projectzulu.common.dungeon;

import org.lwjgl.util.Point;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTBase;

public class GUIEditNodeTextField extends GuiTextField{
	private NBTNode selectedNode = null;
	
	public GUIEditNodeTextField(FontRenderer par1FontRenderer, int xPos, int yPos, int width, int height) {
		super(par1FontRenderer, xPos, yPos, width, height);		
	}
	
	public GUIEditNodeTextField(FontRenderer fontRenderer, int maxTextChars, Point screenSize, Point backgroundSize, Point position, Point boxSize) {
		this(fontRenderer, 
				(screenSize.getX() - (int)backgroundSize.getX())/2+position.getX(),
				(screenSize.getY() - (int)backgroundSize.getY())/2+position.getY(),
				boxSize.getX(), boxSize.getY());
		setupTextField(30000);
	}
	
	public GUIEditNodeTextField(GUIEditNodeTextField oldTextFields, FontRenderer fontRenderer, int maxTextChars, Point screenSize, Point backgroundSize, Point position, Point boxSize) {
		this(fontRenderer, 
				(screenSize.getX() - (int)backgroundSize.getX())/2+position.getX(),
				(screenSize.getY() - (int)backgroundSize.getY())/2+position.getY(),
				boxSize.getX(), boxSize.getY());
		
		this.selectedNode = oldTextFields.selectedNode;
		setText(oldTextFields.getText());
		setupTextField(30000);
	}
	
	private void setupTextField(int maxTextChars){
		setTextColor(-1);
		func_82266_h(-1);
		setMaxStringLength(maxTextChars);
		setEnableBackgroundDrawing(false);
	}
	
	public boolean isEnabled(){
		return selectedNode != null;
	}
	
	public void setSelectedNode(NBTNode selectedNode){
		this.selectedNode = selectedNode;
		setText(selectedNode.getValue());
	}
	
	public boolean saveAndClear(NBTTree nodeTree){
		NBTBase newNBT = selectedNode.createNBTFromString(getText());
		if(newNBT != null){
			if(selectedNode.getParent() != null){
				selectedNode.getParent().replaceChild(selectedNode, new NBTNode(newNBT, selectedNode.getParent())); 
			}else{
				selectedNode = new NBTNode(newNBT, null);
			}
			clear();
			return true;
		}
		return false;
	}
	
	public void clear(){
		selectedNode = null;
		setText("");
	}
	
	@Override
	public boolean textboxKeyTyped(char keyChar, int keyID) {
		if(selectedNode != null){
			return super.textboxKeyTyped(keyChar, keyID);
		}
		return false;
	}
	
	@Override
	public void mouseClicked(int par1, int par2, int par3) {
		if(selectedNode != null){
			super.mouseClicked(par1, par2, par3);
		}
	}
	
	@Override
	public void drawTextBox() {
		super.drawTextBox();
	}	
}
