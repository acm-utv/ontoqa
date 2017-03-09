package com.acmutv.ontoqa.core.lexicon;

public class LSynBehavior {

	
	private static String frame;
	private static boolean frameSubject;
	private static boolean framePossessiveAdjunct;
	private static boolean frameDirectObject;
	private static boolean frameCopulativeSubject;
	private static boolean frameAttributiveArg;
	private static boolean frameAdverbialComplement;
	private static boolean framePrepositionalObject;
	
	
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
	
	public static String getFrame() {
		return frame;
	}
	public static void setFrame(String frame) {
		LSynBehavior.frame = frame;
	}
	public static boolean isFrameSubject() {
		return frameSubject;
	}
	public static void setFrameSubject(boolean frameSubject) {
		LSynBehavior.frameSubject = frameSubject;
	}
	public static boolean isFramePossessiveAdjunct() {
		return framePossessiveAdjunct;
	}
	public static void setFramePossessiveAdjunct(boolean framePossessiveAdjunct) {
		LSynBehavior.framePossessiveAdjunct = framePossessiveAdjunct;
	}
	public static boolean isFrameDirectObject() {
		return frameDirectObject;
	}
	public static void setFrameDirectObject(boolean frameDirectObject) {
		LSynBehavior.frameDirectObject = frameDirectObject;
	}
	public static boolean isFrameCopulativeSubject() {
		return frameCopulativeSubject;
	}
	public static void setFrameCopulativeSubject(boolean frameCopulativeSubject) {
		LSynBehavior.frameCopulativeSubject = frameCopulativeSubject;
	}
	public static boolean isFrameAttributiveArg() {
		return frameAttributiveArg;
	}
	public static void setFrameAttributiveArg(boolean frameAttributiveArg) {
		LSynBehavior.frameAttributiveArg = frameAttributiveArg;
	}
	public static boolean isFrameAdverbialComplement() {
		return frameAdverbialComplement;
	}
	public static void setFrameAdverbialComplement(boolean frameAdverbialComplement) {
		LSynBehavior.frameAdverbialComplement = frameAdverbialComplement;
	}

	public static boolean isFramePrepositionalObject() {
		return framePrepositionalObject;
	}

	public static void setFramePrepositionalObject(boolean framePrepositionalObject) {
		LSynBehavior.framePrepositionalObject = framePrepositionalObject;
	}
	
	
}
