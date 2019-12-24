/**
 * Copyright 2019 Distributed Systems Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package SimBlock.block;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Coinage implements Cloneable {
	private BigInteger coins;
	private long age;

	public Coinage(BigInteger coins, long age) {
		this.coins = coins;
		this.age = age;
	}

	public BigInteger getCoins() { return this.coins; }
	public long getAge() { return this.age; }
	public void increaseAge() { this.age++; }
	public void resetAge() { this.age = 0; }
	public void reward(double reward) { this.coins = this.coins.add(new BigDecimal(this.getCoinage()).multiply(new BigDecimal(reward)).toBigInteger()); }
	
	public BigInteger getCoinage() { return this.getCoins().multiply(BigInteger.valueOf(this.getAge())); }

	@Override
	public Coinage clone() {
		Coinage ret = null;
		try {
			ret = (Coinage)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
