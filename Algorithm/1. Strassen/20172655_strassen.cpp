//Strassen's Matrix Multiplication Algorithm
//컴퓨터학부 (나)반 20172655 이강산 
//UTF-8
#include <iostream>
#include <cstdio>
#define THRESHOLD 2
using namespace std;
int sizeCheck(int inputSize);
void buildMatrix(int**& M, int matrixSize);
void resetMatrix(int**& M, int matrixSize);
void printMatrix(int**& M, int inputSize);
void inputMatrix(int**& M, char N, int inputSize);
void strassen(int matrixSize, int**& A, int**& B, int**& C);
void copySubMatrix(int**& A, int**& a11, int**& a12, int**& a21, int**& a22, int subMatrixSize);
void addMatrix(int matrixSize, int**& temp, int**& m1, int**& m2);
void subMatrix(int matrixSize, int**& temp, int**& m1, int**& m2);
void rebuildMatrix(int**& C, int**& c11, int**& c12, int**& c21, int**& c22, int matrixSize);
void freeMatrix(int**& M, int matrixSize);
void freeStrassen(int**& a11, int**& a12, int**& a21, int**& a22, 
        int**& b11, int**& b12, int**& b21, int**& b22,
        int**& c11, int**& c12, int**& c21, int**& c22,
        int**& m1, int**& m2, int**& m3, int**& m4, int**& m5, int**& m6, int**& m7,
        int**& temp1, int**& temp2, int matrixSize);

int main(){
    int inputSize;
    int matrixSize;
    int **A, **B, **C;
    cout<<"Strassen's Matrix Multiplication Algorithm"<<endl;
    cout<<"input matrice size N (N * N) : ";
    
    cin>>inputSize; //행렬의 크기 입력
    
    matrixSize=sizeCheck(inputSize);
    // 행렬의 크기가 2의 멱수가 아닐 경우 2의 멱수로 조정

    buildMatrix(A, matrixSize);
    buildMatrix(B, matrixSize);
    buildMatrix(C, matrixSize);
    
    inputMatrix(A, 'A', inputSize);
    inputMatrix(B, 'B', inputSize);

    strassen(matrixSize, A, B, C);
    
    printMatrix(C, inputSize);

    freeMatrix(A, matrixSize);
    freeMatrix(B, matrixSize);
    freeMatrix(C, matrixSize);
}

int sizeCheck(int inputSize)
{
    int i=1;
    for(int j=0; i<inputSize; j++)
        i*=2;
    return i;
}

void buildMatrix(int**& M, int matrixSize)
{
    M = (int**)malloc(sizeof(int*)*matrixSize);
    for(int i=0; i<matrixSize; i++)
        M[i]=(int*)malloc(sizeof(int)*matrixSize);
    resetMatrix(M, matrixSize);
}

void freeMatrix(int**& M, int matrixSize)
{
    for(int i=0; i<matrixSize; i++)
        free(M[i]);
    free(M);
}
void freeStrassen(int**& a11, int**& a12, int**& a21, int**& a22, 
        int**& b11, int**& b12, int**& b21, int**& b22,
        int**& c11, int**& c12, int**& c21, int**& c22,
        int**& m1, int**& m2, int**& m3, int**& m4, int**& m5, int**& m6, int**& m7,
        int**& temp1, int**& temp2, int matrixSize)
{
    for(int i=0; i<matrixSize; i++)
    {
        free(a11[i]); free(a12[i]); free(a21[i]); free(a22[i]);
        free(b11[i]); free(b12[i]); free(b21[i]); free(b22[i]);
        free(c11[i]); free(c12[i]); free(c21[i]); free(c22[i]);
        free(m1[i]); free(m2[i]); free(m3[i]); free(m4[i]);
        free(m5[i]); free(m6[i]); free(m7[i]);
        free(temp1[i]); free(temp2[i]);
    }
    free(a11); free(a12); free(a21); free(a22);
    free(b11); free(b12); free(b21); free(b22);
    free(c11); free(c12); free(c21); free(c22);
    free(m1); free(m2); free(m3); free(m4);
    free(m5); free(m6); free(m7);
    free(temp1); free(temp2);
}



void resetMatrix(int**& M, int matrixSize)
{
    for(int i=0; i<matrixSize; i++)
    {
        for(int j=0; j<matrixSize; j++)
            M[i][j]=0;
    }
}

void copySubMatrix(int**& A, int**& a11, int**& a12, int**& a21, int**& a22, int subMatrixSize)
{ //4개의 부분 행렬로 분할
   a11=(int**)malloc(sizeof(int*)*subMatrixSize);
   a12=(int**)malloc(sizeof(int*)*subMatrixSize);
   a21=(int**)malloc(sizeof(int*)*subMatrixSize);
   a22=(int**)malloc(sizeof(int*)*subMatrixSize);
   for(int i=0; i<subMatrixSize; i++)
   {
       a11[i]=(int*)malloc(sizeof(int)*subMatrixSize);
       a12[i]=(int*)malloc(sizeof(int)*subMatrixSize);
       a21[i]=(int*)malloc(sizeof(int)*subMatrixSize);
       a22[i]=(int*)malloc(sizeof(int)*subMatrixSize);
   }
   for(int i=0; i<subMatrixSize; i++)
   {
       for(int j=0; j<subMatrixSize; j++)
       {
           a11[i][j]=A[i][j];
           a12[i][j]=A[i][j+subMatrixSize];
           a21[i][j]=A[i+subMatrixSize][j];
           a22[i][j]=A[i+subMatrixSize][j+subMatrixSize];
       }
   }
}

void printMatrix(int**& M, int inputSize)
{
    for(int i=0; i<inputSize; i++)
    {
        for(int j=0; j<inputSize; j++)
            printf("%-4d", M[i][j]);
        printf("\n");
    }
    printf("*********************\n");
}

void inputMatrix(int**& M, char N, int inputSize)
{
    cout<<"**********(row, col)**********"<<endl;
    cout<<"<< input Matrix "<<N<<endl;
    for(int row=1; row<=inputSize; row++)
    {
        for(int col=1; col<=inputSize; col++) 
        {
            cout<<"("<<row<<", "<<col<<") : ";
            cin>>M[row-1][col-1];
        }
    }
}

void strassen(int matrixSize, int**& A, int**& B, int**& C)
{
    int **m1, **m2, **m3, **m4, **m5, **m6, **m7; //strassen 행렬 곱에 필요한 m1~m7 을 가리킬 포인터 변수들
    int **A11, **A12, **A21, **A22; 
    int **B11, **B12, **B21, **B22;
    int **C11, **C12, **C21, **C22; //A, B, C의 부분 행렬을 가리킬 포인터 변수들
    int **temp1, **temp2; //m1 ~ m7 연산에 필요한 temp 행렬을 가리킬 포인터 변수들
    if(matrixSize<=THRESHOLD) 
    { //행렬의 크기가 임계점보다 작을 경우 naive matrix multiplication 방식 사용 ( #define THRESHOLD 2 )
        int sum=0;
        for(int k=0; k<matrixSize; k++)
            for(int i=0; i<matrixSize; i++)
                for(int j=0; j<matrixSize; j++)
                    C[k][i]+=(A[k][j]*B[j][i]);
    }
    else
    {
        buildMatrix(temp1, matrixSize/2);   
        buildMatrix(temp2, matrixSize/2); // 
        
        buildMatrix(m1, matrixSize/2); 
        buildMatrix(m2, matrixSize/2); 
        buildMatrix(m3, matrixSize/2); 
        buildMatrix(m4, matrixSize/2); 
        buildMatrix(m5, matrixSize/2); 
        buildMatrix(m6, matrixSize/2); 
        buildMatrix(m7, matrixSize/2); 

        copySubMatrix(A, A11, A12, A21, A22, matrixSize/2); //부분 행렬로 분할, a11 ~ a22 에 대한 메모리 할당 이루어짐
        copySubMatrix(B, B11, B12, B21, B22, matrixSize/2);
        copySubMatrix(C, C11, C12, C21, C22, matrixSize/2);
        
        //m1 = (A11+A22)(B11+B22)
        addMatrix(matrixSize/2, temp1, A11, A22);
        addMatrix(matrixSize/2, temp2, B11, B22);
        strassen(matrixSize/2, temp1, temp2, m1);
        resetMatrix(temp1, matrixSize/2);
        resetMatrix(temp2, matrixSize/2);
        //m2 = (A21+A22)B11
        addMatrix(matrixSize/2, temp1, A21, A22);
        strassen(matrixSize/2, temp1, B11, m2);
        resetMatrix(temp1, matrixSize/2);
        //m3 = A11(B12-B22)
        subMatrix(matrixSize/2, temp1, B12, B22);
        strassen(matrixSize/2, A11, temp1, m3);
        resetMatrix(temp1, matrixSize/2);
        //m4 = A22(B21-B11)
        subMatrix(matrixSize/2, temp1, B21, B11);
        strassen(matrixSize/2, A22, temp1, m4);
        resetMatrix(temp1, matrixSize/2);
        //m5 = (A11+A12)B22
        addMatrix(matrixSize/2, temp1, A11, A12);
        strassen(matrixSize/2, temp1, B22, m5);
        resetMatrix(temp1, matrixSize/2);
        //m6 = (A21-A11)(B11+B12)
        subMatrix(matrixSize/2, temp1, A21, A11);
        addMatrix(matrixSize/2, temp2, B11, B12);
        strassen(matrixSize/2, temp1, temp2, m6);
        resetMatrix(temp1, matrixSize/2);
        resetMatrix(temp2, matrixSize/2);
        //m7 = (A12-A22)(B21+B22)
        subMatrix(matrixSize/2, temp1, A12, A22);
        addMatrix(matrixSize/2, temp2, B21, B22);
        strassen(matrixSize/2, temp1, temp2, m7);
        resetMatrix(temp1, matrixSize/2);
        resetMatrix(temp2, matrixSize/2);

        //c11 = m1+m4-m5+m7
        addMatrix(matrixSize/2, temp1, m1, m4);
        subMatrix(matrixSize/2, temp1, temp1, m5);
        addMatrix(matrixSize/2, temp1, temp1, m7);
        addMatrix(matrixSize/2, C11, temp1, C11);
        resetMatrix(temp1, matrixSize/2);
        //c12 = m3+m5
        addMatrix(matrixSize/2, temp1, m3, m5);
        addMatrix(matrixSize/2, C12, temp1, C12);
        resetMatrix(temp1, matrixSize/2);
        //c21 = m2+m4
        addMatrix(matrixSize/2, temp1, m2, m4);
        addMatrix(matrixSize/2, C21, temp1, C21);
        resetMatrix(temp1, matrixSize/2);
        //c22 = m1+m3-m2+m6
        addMatrix(matrixSize/2, temp1, m1, m3);
        subMatrix(matrixSize/2, temp1, temp1, m2);
        addMatrix(matrixSize/2, temp1, temp1, m6);
        addMatrix(matrixSize/2, C22, temp1, C22);
        resetMatrix(temp1, matrixSize/2);
        //c11 ~ c22 로 C 생성
        rebuildMatrix(C, C11, C12, C21, C22, matrixSize/2);
        freeStrassen(A11, A12, A21, A22, B11, B12, B21, B22, C11, C12, C21, C22, m1, m2, m3, m4, m5, m6, m7, temp1, temp2, matrixSize/2);
    }
}

void addMatrix(int matrixSize, int**& temp, int**& m1, int**& m2)
{
    for(int i=0; i<matrixSize; i++)
        for(int j=0; j<matrixSize; j++)
            temp[i][j]=m1[i][j]+m2[i][j];
}

void subMatrix(int matrixSize, int**& temp, int**& m1, int**& m2)
{
    for(int i=0; i<matrixSize; i++)
        for(int j=0; j<matrixSize; j++)
            temp[i][j]=m1[i][j]-m2[i][j];
}

void rebuildMatrix(int**& C, int**& c11, int**& c12, int**& c21, int**& c22, int matrixSize)
{
    for(int i=0; i<matrixSize; i++)
    {
        for(int j=0; j<matrixSize; j++)
        {
            C[i][j]=c11[i][j];
            C[i][j+matrixSize]=c12[i][j];
            C[i+matrixSize][j]=c21[i][j];
            C[i+matrixSize][j+matrixSize]=c22[i][j];
        }
    }
}
