/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.behavior.world;

public class Field {
	public static final int WALL_MASK = 0x01;
	public static final int FOOD_MASK = 0x02;
	public static final int PHERO_MASK = 0x04;

	private int properties;

	public Field() {
		this.properties = 0; // field is empty
	}

	public Field(int properties) {
		this.properties = properties;
	}

	public boolean isEmpty() {
		return properties == 0;
	}

	public boolean isWall() {
		return (properties & WALL_MASK) != 0;
	}

	public boolean isFood() {
		return (properties & FOOD_MASK) != 0;
	}
	
	public boolean isPhero() {
		return (properties & PHERO_MASK) != 0;
	}
	
	public void setFood() {
		properties |= FOOD_MASK;
	}
	
	public void setPhero() {
		properties |= PHERO_MASK;
	}

	public void removeFood() {
		properties = properties & (~FOOD_MASK);
	}
	
	public int getProperties() {
		return properties;
	}
}