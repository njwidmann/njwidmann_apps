package org.andengine.entity.shape;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.util.algorithm.collision.RectangularShapeCollisionChecker;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich, Nick Widmann
 * @since 11:37:50 - 04.04.2010, 2015
 */
public abstract class RectangularShape extends Shape implements IAreaShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public enum Side {LEFT, RIGHT, TOP, BOTTOM, CENTER}

	protected float mWidth;
	protected float mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangularShape(final float pX, final float pY, final float pWidth, final float pHeight, final ShaderProgram pShaderProgram) {
		super(pX, pY, pShaderProgram);

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.resetRotationCenter();
		this.resetScaleCenter();
		this.resetSkewCenter();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Overloaded method to set center, left, and rightmost x values
	 * @param side Side.CENTER, Side.RIGHT or Side.LEFT
	 * @param x the x value
	 */
	public void setX(Side side, float x) {
		if(side == Side.RIGHT) {
			setX(x - this.getWidth());
		} else if (side == Side.CENTER) {
			setX(x - this.getWidth() / 2);
		} else {
			setX(x);
		}
	}

	/**
	 * Overloaded method to set the center, top, and bottommost y values
	 * @param side Side.CENTER, Side.TOP or Side.BOTTOM
	 * @param y the y value
	 */
	public void setY(Side side, float y) {
		if(side == Side.BOTTOM) {
			setY(y - this.getHeight());
		} else if (side == Side.CENTER) {
			setY(y - this.getHeight() / 2);
		} else {
			setY(y);
		}
	}




	/**
	 * Overloaded method to get center, left, and rightmost x values
	 * @param side Side.CENTER, Side.RIGHT or Side.LEFT
	 * @return the x value
	 */
	public float getX(Side side) {
		if(side == Side.RIGHT) {
			return this.getX() + this.getWidth();
		} else if (side == Side.CENTER) {
			return this.getX() + this.getWidth() / 2;
		} else {
			return this.getX();
		}
	}

	/**
	 * Overloaded method to get the center, top, and bottommost y values
	 * @param side Side.CENTER, Side.TOP or Side.BOTTOM
	 * @return the y value
	 */
	public float getY(Side side) {
		if(side == Side.BOTTOM) {
			return this.getY() + this.getHeight();
		} else if (side == Side.CENTER) {
			return this.getY() + this.getHeight() / 2;
		} else {
			return this.getY();
		}
	}


	@Override
	public float getWidth() {
		return this.mWidth;
	}

	@Override
	public float getBaseWidth() {return this.mWidth;}

	@Override
	public float getHeight() {
		return this.mHeight;
	}

	@Override
	public float getBaseHeight() {
		return this.mHeight;
	}

	@Override
	public void setWidth(final float pWidth) {
		this.mWidth = pWidth;
		this.onUpdateVertices();
	}

	@Override
	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;
		this.onUpdateVertices();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.onUpdateVertices();
	}

	@Override
	public float getWidthScaled() {
		return this.getWidth() * this.mScaleX;
	}

	@Override
	public float getHeightScaled() {
		return this.getHeight() * this.mScaleY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isCulled(final Camera pCamera) {
		return !RectangularShapeCollisionChecker.isVisible(pCamera, this);
	}

	@Override
	public void reset() {
		super.reset();

		this.resetRotationCenter();
		this.resetSkewCenter();
		this.resetScaleCenter();
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return RectangularShapeCollisionChecker.checkContains(this, pX, pY);
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.convertLocalToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f);
	}

	@Override
	public float[] getSceneCenterCoordinates(final float[] pReuse) {
		return this.convertLocalToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f, pReuse);
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof RectangularShape) {
			return RectangularShapeCollisionChecker.checkCollision(this, (RectangularShape) pOtherShape);
		} else if(pOtherShape instanceof Line) {
			return RectangularShapeCollisionChecker.checkCollision(this, (Line) pOtherShape);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void resetRotationCenter() {
		this.mRotationCenterX = this.mWidth * 0.5f;
		this.mRotationCenterY = this.mHeight * 0.5f;
	}

	public void resetScaleCenter() {
		this.mScaleCenterX = this.mWidth * 0.5f;
		this.mScaleCenterY = this.mHeight * 0.5f;
	}

	public void resetSkewCenter() {
		this.mSkewCenterX = this.mWidth * 0.5f;
		this.mSkewCenterY = this.mHeight * 0.5f;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
