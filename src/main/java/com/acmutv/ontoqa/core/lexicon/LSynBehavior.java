package com.acmutv.ontoqa.core.lexicon;

public class LSynBehavior {

	
	private String frame;
	private boolean frameSubject;
	private boolean framePossessiveAdjunct;
	private boolean frameDirectObject;
	private boolean frameCopulativeSubject;
	private boolean frameAttributiveArg;
	private boolean frameAdverbialComplement;
	private boolean framePrepositionalObject;
	
	
	public LSynBehavior() {
		frameSubject= false;
		framePossessiveAdjunct= false;
		frameDirectObject = false;
		frameCopulativeSubject = false;
		frameAttributiveArg = false;
		frameAdverbialComplement = false;
		framePrepositionalObject = false;
		// TODO Auto-generated constructor stub
	}
	
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public boolean isFrameSubject() {
		return frameSubject;
	}
	public void setFrameSubject(boolean frameSubject) {
		this.frameSubject = frameSubject;
	}
	public boolean isFramePossessiveAdjunct() {
		return framePossessiveAdjunct;
	}
	public void setFramePossessiveAdjunct(boolean framePossessiveAdjunct) {
		this.framePossessiveAdjunct = framePossessiveAdjunct;
	}
	public boolean isFrameDirectObject() {
		return frameDirectObject;
	}
	public void setFrameDirectObject(boolean frameDirectObject) {
		this.frameDirectObject = frameDirectObject;
	}
	public boolean isFrameCopulativeSubject() {
		return frameCopulativeSubject;
	}
	public void setFrameCopulativeSubject(boolean frameCopulativeSubject) {
		this.frameCopulativeSubject = frameCopulativeSubject;
	}
	public boolean isFrameAttributiveArg() {
		return frameAttributiveArg;
	}
	public void setFrameAttributiveArg(boolean frameAttributiveArg) {
		this.frameAttributiveArg = frameAttributiveArg;
	}
	public boolean isFrameAdverbialComplement() {
		return frameAdverbialComplement;
	}
	public void setFrameAdverbialComplement(boolean frameAdverbialComplement) {
		this.frameAdverbialComplement = frameAdverbialComplement;
	}

	public boolean isFramePrepositionalObject() {
		return framePrepositionalObject;
	}

	public void setFramePrepositionalObject(boolean framePrepositionalObject) {
		this.framePrepositionalObject = framePrepositionalObject;
	}
	
	
}
