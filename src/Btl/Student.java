package Btl;

public class Student {
	private String name;
	private String mssv;
	private double diem1;
	private double diem2;
	private double diem3;
	
	public Student(String name,String mssv,double diem1,double diem2,double diem3) {
		this.name = name;
		this.mssv = mssv;
		this.diem1=diem1;
		this.diem2=diem2;
		this.diem3=diem3;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMssv() {
		return mssv;
	}
	public void setMssv(String mssv) {
		this.mssv = mssv;
	}

	public double getDiem1() {
		return diem1;
	}
	public void setDiem1(double diem1) {
		this.diem1 = diem1;
	}
	public double getDiem2() {
		return diem2;
	}
	public void setDiem2(double diem2) {
		this.diem2 = diem2;
	}
	public double getDiem3() {
		return diem3;
	}
	public void setDiem3(double diem3) {
		this.diem3 = diem3;
	}
	public double getDiemhocphan() {
		return (diem1*0.1+diem2*0.3+diem3*0.6);
	}

	}


