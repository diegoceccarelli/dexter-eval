///**
// *  Copyright 2014 Diego Ceccarelli
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// * 
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
///**
// *  Copyright 2014 Diego Ceccarelli
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// * 
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package it.cnr.isti.hpc.dexter.eval.output;
//
///**
// * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
// * 
// *         Created on Feb 17, 2014
// */
//public class Result {
//
//	private String name;
//	private double value;
//
//	public Result(String name, double value) {
//		super();
//		this.name = name;
//		this.value = value;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public double getValue() {
//		return value;
//	}
//
//	public void setValue(double value) {
//		this.value = value;
//	}
//
//	public String getFormatedResult() {
//		return String.format("%-40s%.3f", name, value);
//	}
//
//	public static void main(String[] args) {
//		Result r = new Result("maradona", 0.9999123912391923131);
//		Result r1 = new Result("maradonamaradonamaradona", 0.41);
//		Result r2 = new Result("p", 0.9929123912391923131);
//		Result r3 = new Result("p3", 0.001);
//		System.out.println(r.getFormatedResult());
//		System.out.println(r1.getFormatedResult());
//		System.out.println(r2.getFormatedResult());
//		System.out.println(r3.getFormatedResult());
//	}
// }
