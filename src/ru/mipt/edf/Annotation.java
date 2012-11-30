/*
 * (The MIT license)
 * 
 * Copyright (c) 2012 MIPT (mr.santak@gmail.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.mipt.edf;

import java.util.ArrayList;
import java.util.List;

public class Annotation
{
	private double onSet = 0;
	private double duration = 0;
	private List<String> annotations = new ArrayList<String>();

	public static List<Annotation> parseAnnotations(byte[] b)
	{
		List<Annotation> annotations = new ArrayList<Annotation>();
		int onSetIndex = 0;
		int durationIndex = -1;
		int annotationIndex = -2;
		int endIndex = -3;
		for (int i = 0; i < b.length - 1; i++)
		{
			if (b[i] == 21)
			{
				durationIndex = i;
				continue;
			}
			if (b[i] == 20 && onSetIndex > annotationIndex)
			{
				annotationIndex = i;
				continue;
			}
			if (b[i] == 20 && b[i + 1] == 0)
			{
				endIndex = i;
				continue;
			}
			if (b[i] != 0 && onSetIndex < endIndex)
			{

				String onSet = null;
				String duration = null;
				if (durationIndex > onSetIndex)
				{
					onSet = new String(b, onSetIndex, durationIndex - onSetIndex);
					duration = new String(b, durationIndex, annotationIndex - durationIndex);
				} else
				{
					onSet = new String(b, onSetIndex, annotationIndex - onSetIndex);
					duration = "";
				}
				String annotation = new String(b, annotationIndex, endIndex - annotationIndex);
				annotations.add(new Annotation(onSet, duration, annotation.split("[\u0014]")));
				onSetIndex = i;
			}
		}
		return annotations;
	}

	private Annotation(String onSet, String duration, String[] annotations)
	{
		this.onSet = Double.parseDouble(onSet);
		if (duration != null && duration != "")
			this.duration = Double.parseDouble(duration);
		for (int i = 0; i < annotations.length; i++)
		{
			if (annotations[i] == null || annotations[i].trim().equals(""))
				continue;
			this.annotations.add(annotations[i]);
		}
	}

	public double getOnSet()
	{
		return onSet;
	}

	public double getDuration()
	{
		return duration;
	}

	public List<String> getAnnotations()
	{
		return annotations;
	}

	@Override
	public String toString()
	{
		return "Annotation [onSet=" + onSet + ", duration=" + duration + ", annotations=" + annotations + "]";
	}
}
