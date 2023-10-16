#include <iostream>

using std::cin;
using std::cout;
using std::endl;

void divide(int a1, int a2, int &quotient, int &remainder);

int main()
{
	int num1, num2, q, r;
	cout<<"두 개의 숫자 입력 : ";
	cin>>num1>>num2;
	try
	{
		if(num2==0)
			throw num2;
	}
	catch(int expn)
	{
		cout<<"제수는 0이 될 수 없습니다."<<endl<<"프로그램을 종료합니다."<<endl;
		return 0;
	}
	divide(num1, num2, q, r); 
	cout<<num1<<" / "<<num2<<" = "<<q<<", "<<num1<<" % "<<num2<<" = "<<r<<endl;
	return 0;
}

void divide(int a1, int a2, int &quotient, int &remainder)
{
	quotient=a1/a2;
	remainder=a1%a2;
}

