//Optimal Binary Search Tree
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>

void buildArray(int n, float*& p, float**& A, int**& R);
void destroyArray(int n, float*& p, float**& A, int**& R);
void printArrayA(int n, float**& A);
void printArrayR(int n, int**& R);
void optsearchtree(int n, float*& p, float& minavg, float**& A, int**& R);
float sumP(float*& p, int i, int j);

int main(void)
{
    int n;         //트리의 노드 개수 (node1 ~ nodeN)
    float* p;      //각 키가 검색될 확률을 저장한 배열 
    float** A;     //A[i][j]의 값은 i~j 노드를 포함한 최적트리의 평균검색시간 
    int** R;       //R[i][j]의 값은 i~j 노드를 포함한 최적트리의 루트노드의 인덱스
    float minavg;  //최적이진탐색트리의 평균탐색시간 저장 (A[1][n] 값)
    
    printf("Optimal Bianry Search Tree\n");
    printf("20172655 LEE KANG SAN\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");

    printf("트리의 노드 개수(n) : ");
    scanf("%d", &n);
    buildArray(n, p, A, R);
    printf("*** *** *** *** *** *** *** *** *** ***\n");

    printf("p 배열 입력\n");
    for(int i=1; i<=n; i++)
    {
        printf("p[%d] : ", i);
        scanf("%f", &p[i]);
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");

    optsearchtree(n, p, minavg, A, R);
    
    printArrayA(n, A);
    
    printArrayR(n, R);

    destroyArray(n, p, A, R);
    return 0;
}
//p배열의 경우 key의 index가 1부터 시작이므로 n+1 size를 갖는다
//A, R배열의 경우 (n+2)*(n+1) size를 갖는다
void buildArray(int n, float*& p, float**& A, int**& R)
{
    p=(float*)malloc(sizeof(float)*(n+1));
    A=(float**)malloc(sizeof(float*)*(n+2));
    R=(int**)malloc(sizeof(int*)*(n+2));
    for(int i=0; i<=n+1; i++)
    {
        A[i]=(float*)malloc(sizeof(float)*(n+1));
        R[i]=(int*)malloc(sizeof(int)*(n+1));
    }
    for(int i=0; i<=n+1; i++)
    {
        for(int j=0; j<=n; j++)
        {
            A[i][j]=0;
            R[i][j]=0;
        }
    }  
}

void destroyArray(int n, float*& p, float**& A, int**& R)
{
    free(p);
    for(int i=0; i<=n+1; i++)
    {
        free(A[i]);
        free(R[i]);
    }
    free(A);
    free(R);
    printf("<끝>\n");
}

void printArrayA(int n, float**& A)
{
    printf("< A 배열 >\n");
    for(int i=1; i<=n+1; i++)
    {
        for(int j=0; j<=n; j++)
        {
            if(j<=i-2)
                printf("       ");
            else
                printf("%-.3f  ", A[i][j]);
        }
        printf("\n");
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
}
void printArrayR(int n, int**& R)
{
    printf("< R 배열 >\n");
    for(int i=1; i<=n+1; i++)
    {
        for(int j=0; j<=n; j++)
        {
            if(j<=i-2)
                printf("   ");
            else
                printf("%-3d", R[i][j]);
        }
        printf("\n");
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
}

void optsearchtree(int n, float*& p, float& minavg, float**& A, int**& R)
{
    int i, j, k, diagonal;
    for(i=1; i<=n; i++)
    {
        A[i][i-1]=0;
        A[i][i]=p[i];
        R[i][i]=i;
        R[i][i-1]=0;
    }
    A[n+1][n]=0;
    R[n+1][n]=0;
    
    for(diagonal=1; diagonal<=n-1; diagonal++)
    {
        for(i=1; i<=n-diagonal; i++)
        {
            j=i+diagonal;
            for(k=i; k<=j; k++)
            {
                if(k==i)
                {
                    A[i][j]=(A[i][k-1]+A[k+1][j])+sumP(p, i, j);
                    R[i][j]=k;
                }
                else if(A[i][j]>(A[i][k-1]+A[k+1][j]+sumP(p, i, j)))
                {
                    A[i][j]=A[i][k-1]+A[k+1][j]+sumP(p, i, j);
                    R[i][j]=k;
                }
            }
        }
    }
    minavg=A[1][n];
}

float sumP(float*& p, int i, int j)
{
    float sum = 0.;
    for(i; i<=j; i++)
        sum+=p[i];
    return sum;
}
                       



















