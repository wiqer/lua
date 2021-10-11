/*******************************************************************************
* Copyright (c) 2009-2011 Luaj.org. All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
******************************************************************************/
package com.wiqer.lua.lib.jse;

import com.wiqer.lua.Globals;
import com.wiqer.lua.LuaValue;
import com.wiqer.lua.lib.LibFunction;
import com.wiqer.lua.lib.MathLib;
import com.wiqer.lua.lib.TwoArgFunction;

/** 
 * Subclass of {@link LibFunction} which implements the lua standard {@code math} 
 * library. 
 * <p> 
 * It contains all lua math functions, including those not available on the JME platform.  
 * See {@link lib.MathLib} for the exception list.  
 * <p>
 * Typically, this library is included as part of a call to 
 * {@link lib.jse.JsePlatform#standardGlobals()}
 * <pre> {@code
 * Globals globals = JsePlatform.standardGlobals();
 * System.out.println( globals.get("math").get("sqrt").call( LuaValue.valueOf(2) ) );
 * } </pre>
 * <p>
 * For special cases where the smallest possible footprint is desired, 
 * a minimal set of libraries could be loaded
 * directly via {@link Globals#load(LuaValue)} using code such as:
 * <pre> {@code
 * Globals globals = new Globals();
 * globals.load(new JseBaseLib());
 * globals.load(new PackageLib());
 * globals.load(new JseMathLib());
 * System.out.println( globals.get("math").get("sqrt").call( LuaValue.valueOf(2) ) );
 * } </pre>
 * <p>However, other libraries such as <em>CoroutineLib</em> are not loaded in this case.
 * <p>
 * This has been implemented to match as closely as possible the behavior in the corresponding library in C.
 * @see LibFunction
 * @see lib.jse.JsePlatform
 * @see lib.jme.JmePlatform
 * @see JseMathLib
 * @see <a href="http://www.lua.org/manual/5.2/manual.html#6.6">Lua 5.2 Math Lib Reference</a>
 */
public class JseMathLib extends MathLib {
	
	public JseMathLib() {}


	/** Perform one-time initialization on the library by creating a table
	 * containing the library functions, adding that table to the supplied environment,
	 * adding the table to package.loaded, and returning table as the return value.
	 * <P>Specifically, adds all library functions that can be implemented directly
	 * in JSE but not JME: acos, asin, atan, atan2, cosh, exp, log, pow, sinh, and tanh.
	 * @param modname the module name supplied if this is loaded via 'require'.
	 * @param env the environment to load into, which must be a Globals instance.
	 */
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		super.call(modname, env);
		LuaValue math = env.get("math");
		math.set("acos", new acos());
		math.set("asin", new asin());
		LuaValue atan =  new atan2();
		math.set("atan", atan);
		math.set("atan2", atan);
		math.set("cosh", new cosh());
		math.set("exp", new exp());
		math.set("log", new log());
		math.set("pow", new pow());
		math.set("sinh", new sinh());
		math.set("tanh", new tanh());
		return math;
	}

	static final class acos extends UnaryOp { @Override
	protected double call(double d) { return Math.acos(d); } }
	static final class asin extends UnaryOp { @Override
	protected double call(double d) { return Math.asin(d); } }
	static final class atan2 extends TwoArgFunction { 
		@Override
		public LuaValue call(LuaValue x, LuaValue y) {
			return valueOf(Math.atan2(x.checkdouble(), y.optdouble(1)));
		} 
	}
	static final class cosh extends UnaryOp { @Override
	protected double call(double d) { return Math.cosh(d); } }
	static final class exp extends UnaryOp { @Override
	protected double call(double d) { return Math.exp(d); } }
	static final class log extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue x, LuaValue base) {
			double nat = Math.log(x.checkdouble());
			double b = base.optdouble(Math.E);
			if (b != Math.E) {
				nat /= Math.log(b);
			}
			return valueOf(nat);
		}
	}
	static final class pow extends BinaryOp { @Override
	protected double call(double x, double y) { return Math.pow(x, y); } }
	static final class sinh extends UnaryOp { @Override
	protected double call(double d) { return Math.sinh(d); } }
	static final class tanh extends UnaryOp { @Override
	protected double call(double d) { return Math.tanh(d); } }

	/** Faster, better version of pow() used by arithmetic operator ^ */
	@Override
    public double dpow_lib(double a, double b) {
		return Math.pow(a, b);
	}
	
	
}
