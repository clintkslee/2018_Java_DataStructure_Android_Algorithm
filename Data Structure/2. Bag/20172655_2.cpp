#include <iostream>
#include <cstring>
#include <ctime>
#include <algorithm>
using namespace std;
class Bizcard {
private:
	char* name;
	char* phone;
public:
	Bizcard(){};
	Bizcard(const char* inputName, const char* inputPhoneNumber) {
		name = new char[strlen(inputName)+1];
		phone = new char[strlen(inputPhoneNumber)+1];
		strcpy(name, inputName);
		strcpy(phone, inputPhoneNumber);
	}
	Bizcard(const Bizcard& B) { 
		name = new char[strlen(B.name) + 1];
		phone = new char[strlen(B.phone) + 1];
		strcpy(name, B.name);
		strcpy(phone, B.phone);
	}
	Bizcard& operator=(const Bizcard& B) {
		delete[] name;
		delete[] phone;
		name = new char[strlen(B.name) + 1];
		phone = new char[strlen(B.phone) + 1];
		strcpy(name, B.name);
		strcpy(phone, B.phone);
		return *this;
	}
	~Bizcard() {
		delete[] name;
		delete[] phone;
	}
	void ShowInfo(void) {
		cout << name << "  " << phone << endl;
	}
};

template <typename T>
class Bag
{
public:
	Bag(int bagCapacity = 3) :capacity(bagCapacity) {
		array = new T[capacity];
		top = -1;
	}
	~Bag() {
		delete[] array;
	}
	int Size() const {
		return top + 1;
	}
	bool IsEmpty() const {
		if (!Size()) return true;
		return false;
	}
	T& Element() const {
		if (IsEmpty()) throw "Bag is empty, cannot show a random Bizcard";
		return array[rand() % (Size())];
	}
	void Push(const T& input) {
		if (top == capacity - 1) {
			changeSize1D(array, capacity, 2 * capacity);
			capacity *= 2;
		}
		array[++top] = input;
	}
	void Pop() {
		if (IsEmpty()) throw "Bag is empty, cannot Pop";
		int select = rand() % (top+1);
		copy(array + select + 1, array + top + 1, array + select);
	  	top--;	
	}
	void changeSize1D(T*& array, int prevSize, int newSize) {
		T* temp = new T[newSize];
		int num=min(prevSize, newSize);
		copy(array, array+num, temp);
		delete[] array;
		array = temp;
	}
private:
	T *array;
	int capacity;
	int top;
};

int main() {
	srand(time(NULL));
	Bag<Bizcard> bag;
	Bizcard a("Lee KS", "010-1111-1111");
	Bizcard b("Ryu HJ", "010-2222-2222");
	Bizcard c("Ma JW", "010-3333-3333");
	Bizcard d("Kang RS", "010-4444-4444");
	Bizcard e("Kim IS", "010-5555-5555");
	Bizcard f("Nam KH", "010-6666-6666");
	Bizcard g("Yu JJ", "010-7777-7777");
	cout<<"****************"<<endl;
	cout<<"Bag Size() : "<<bag.Size()<<endl;
	cout<<"Bag IsEmpty()(0:not Empty, 1:Empty) : "<<bag.IsEmpty()<<endl;
	try{(bag.Element()).ShowInfo();}catch(const char* msg){cout<<msg<<endl;};
	cout<<"****************"<<endl;
	bag.Push(a);
	bag.Push(b);
	bag.Push(c);
	cout<<"Bag Size() : "<<bag.Size()<<endl;
	cout<<"Bag IsEmpty()(0:not Empty, 1:Empty) : "<<bag.IsEmpty()<<endl;
	try{(bag.Element()).ShowInfo();}catch(const char* msg){cout<<msg<<endl;};
	cout<<"****************"<<endl;
	bag.Push(d);
	bag.Push(e);
	bag.Push(f);
	bag.Push(g);
	cout<<"Bag Size() : "<<bag.Size()<<endl;
	cout<<"Bag IsEmpty()(0:not Empty, 1:Empty) : "<<bag.IsEmpty()<<endl;
	try{(bag.Element()).ShowInfo();}catch(const char* msg){cout<<msg<<endl;};
	cout<<"****************"<<endl;	
	bag.Pop();
	bag.Pop();
	cout<<"Bag Size() : "<<bag.Size()<<endl;
	cout<<"Bag IsEmpty()(0:not Empty, 1:Empty) : "<<bag.IsEmpty()<<endl;
	try{(bag.Element()).ShowInfo();}catch(const char* msg){cout<<msg<<endl;};
	return 0;
}
