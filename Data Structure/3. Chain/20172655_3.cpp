#include <iostream>
using namespace std;

template <typename T> class Chain;

template <typename T>
class ChainNode {
    friend class Chain<T>;
    public:
        ChainNode(T data){
            this->data = data; 
            link=NULL;
        }
    private:
        T data;
        ChainNode<T>* link;
};

template <typename T>
class Chain {
    public:
        Chain(){first = NULL;}
        ~Chain(){};
        void InsertFront(const T& e);
        void InsertBack(const T& e);
        T Front();
        T Back();
        T Get(int i);
        void DeleteFront();
        void DeleteBack();
        void InsertNodeAfter(int i, T data);
        void DeleteNodeAt(int i);
        void PrintChain();

        class ChainIterator {
            friend class Chain<T>;
            public:
                ChainIterator(ChainNode <T>* startNode = 0) {current = startNode;}
                T& operator*()const {return current->data;}
                T* operator->()const {return &current->data;}
                ChainIterator& operator++() {current=current->link; return *this;}
                ChainIterator operator++(int) {
                    ChainIterator old = *this;
                    current = current->link;
                    return old;
                }
                bool operator!=(const ChainIterator right)const
                {return current!=right.current;}
                bool operator==(const ChainIterator right)const
                {return current == right.current;}
            private:
                ChainNode <T>* current;
        };
        ChainIterator begin() {return ChainIterator(first);}
        ChainIterator end() {return ChainIterator(0);}
    private:
        ChainNode<T>* first;
};

template <typename T>
void Chain<T>::PrintChain() {
    ChainIterator ci;
    for(ci=begin(); ci!=end(); ci++)
        cout<<*ci<<"  ";
}
template <typename T>
void Chain<T>::InsertFront(const T& e) {
    if(!first)
        first=new ChainNode<T>(e);
    else{
        ChainNode <T>* temp;
        temp = first;
        first=new ChainNode<T>(e);
        first->link=temp;
    }
}
template <typename T>
void Chain<T>::InsertBack(const T& e) {
    if(!first)
        first=new ChainNode<T>(e);
    else {
        ChainIterator ci;
        for(ci=begin(); ci.current->link!= 0; ci++);
        ci.current->link = new ChainNode<T>(e);
    }
}
template <typename T>
T Chain<T>::Front() {
    if(!first) throw "Chain is Empty\n";
    ChainIterator ci=begin();
    return *ci;
} 
template <typename T>
T Chain<T>::Back() {
    if(!first) throw "Chain is Empty.\n";
    ChainIterator ci;
    for(ci=begin(); ci.current->link!= 0; ci++);
    return *ci;
} 
template <typename T>
T Chain<T>::Get(int i) {
    if(i<0) throw "Wrong position.\n";
    ChainIterator ci=begin();
    for(int j=0; j<i; j++) {
        ci++;
        if(ci.current==NULL) throw "Wrong position.\n";
    }
    return *ci;
}
template <typename T>
void Chain<T>::DeleteFront() {
    if(!first) throw "Chain is Empty.\n";
    ChainNode <T>* temp = first->link;
    delete first;
    first = temp;
}
template <typename T>
void Chain<T>::DeleteBack() {
    if(!first) throw "Chain is Empty.\n";
    ChainIterator ci;
    for(ci=begin(); ci.current->link->link!= 0; ci++);
    delete ci.current->link;
    ci.current->link=NULL;
}
template <typename T>
void Chain<T>::InsertNodeAfter(int i, T data) {
    if(!first) throw "Chain is Empty.\n";
    if(i<0) throw "Wrong position.\n";
    ChainNode <T>* temp;
    ChainIterator ci=begin();
    for(int j=0; j<i; j++) {
        if(ci.current->link==NULL) throw "Wrong position.\n";
        ci++;
    }
    temp = ci.current->link;
    ci.current->link = new ChainNode<T>(data);
    ci.current->link->link=temp;
}
template <typename T>
void Chain<T>::DeleteNodeAt(int i) {
    if(!first) throw "Chain is Empty.\n";
    if(i<0) throw "Wrong position.\n";
    ChainIterator ci=begin();
    if(i==0) {
        first = first->link;
        delete ci.current;
        return;
    }
    ChainNode <T>* temp;
    for(int j=0; j<i-1; j++) {
        if(ci.current->link->link==NULL) throw "Wrong position.\n";
        ci++;
    }
    if(ci.current->link->link==0) {
        delete ci.current->link;
        ci.current->link=NULL;
    }
    else {
        temp=ci.current->link->link;
        delete ci.current->link;
        ci.current->link=temp;
    }
}

int main() {
    Chain<int> c;
    cout<<"\n*************************************\n";
    cout<<"Inserted 1~9 : \n";
    c.InsertFront(1);
    c.InsertBack(2);
    c.InsertBack(3);
    c.InsertBack(4);
    c.InsertBack(5);
    c.InsertBack(6);
    c.InsertBack(7);
    c.InsertBack(8);
    c.InsertBack(9);
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called InsertFront(0) : \n";
    c.InsertFront(0);
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called InsertBack(10) : \n";
    c.InsertBack(10);
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called Front() : \n";
    try{cout<<c.Front();}catch(const char* msg){cout<<msg;}
    cout<<"\n\n";
    cout<<"Called Back() : \n";
    try{cout<<c.Back();}catch(const char* msg){cout<<msg;}
    cout<<"\n\n";
    cout<<"Called Get(2) : \n";
    try{cout<<c.Get(2);}catch(const char* msg){cout<<msg;}
    cout<<"\n\n";
    cout<<"Called DeleteFront() : \n";
    try{c.DeleteFront();}catch(const char* msg){cout<<msg;}
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called DeleteBack() : \n";
    try{c.DeleteBack();}catch(const char* msg){cout<<msg;}
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called InsertNodeAfter(2, 100) : \n";
    try{c.InsertNodeAfter(2, 100);}catch(const char* msg){cout<<msg;}
    c.PrintChain();
    cout<<"\n\n";
    cout<<"Called DeleteNodeAt(6) : \n";
    try{c.DeleteNodeAt(6);}catch(const char* msg){cout<<msg;}
    c.PrintChain();
    cout<<"\n*************************************\n";
    return 0;
}





