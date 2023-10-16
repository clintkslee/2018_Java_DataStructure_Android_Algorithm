//Chained Matrix Multiplication
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
void buildMatrix(int n, int**& M, int**& P);
void printMatrix(int n, int**& M);
int minmult(int n, int*& d, int**& M, int**& P);
void order(int i, int j, int**& P);

int main()
{
    int n;   //행렬 개수
    int* d;  //d0 ~ dn 변수, i번째 행렬의 규모는 d[i-1]*d[i] 로 표현
    int** M; //M[i][j] 는 i번행렬부터 j번 행렬까지 곱하는 데 필요한 최소 곱셈 수 
    int** P; //최적의 순서를 얻을 수 있는 배열 P
    int minmultReturn;

    printf("Chained Matrix Multiplication\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("행렬의 개수(n) 입력 : ");
    scanf("%d", &n);
    buildMatrix(n, M, P);
    d=(int*)malloc(sizeof(int)*(n+1));
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("배열 크기 입력 (d0 ~ dn)\n");
    for(int i=0; i<=n; i++)
    {
        printf("d[%d] : ", i);
        scanf("%d", &d[i]);
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    minmultReturn = minmult(n, d, M, P); //minmult 알고리즘 수행
    printf("<M배열 출력>\n");
    printMatrix(n, M); 
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<P배열 출력>\n");
    printMatrix(n, P); 
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("A1~A%d 최적 분해 : ", n);
    order(1, n, P); //A1~An 곱하는 순서 출력
    printf("\n\n<끝>\n");
    // 동적할당 메모리 해제
    for(int i=0; i<=n; i++)
    {
        free(M[i]);
        free(P[i]);
    }
    free(M);
    free(P);
    free(d);
}
void buildMatrix(int n, int**& M, int**& P)
{
    M=(int**)malloc(sizeof(int*)*(n+1));
    P=(int**)malloc(sizeof(int*)*(n+1));
    for(int i=0; i<=n; i++)
    {
        M[i]=(int*)malloc(sizeof(int)*(n+1));
        P[i]=(int*)malloc(sizeof(int)*(n+1));
    }
}

void printMatrix(int n, int**& M)
{
    for(int i=1; i<=n; i++)
    {
        for(int j=1; j<=n; j++)
        {
            if(i<=j)
                printf("%-5d", M[i][j]);
            else
                printf("     ");
        }
        printf("\n");
    }
}
int minmult(int n, int*& d, int**& M, int**& P)
{
    int i, j, k, diagonal;
    for(i=1; i<=n; i++)
        M[i][i] = 0;
    for(diagonal = 1; diagonal <=n-1; diagonal++)
    {
        for(i=1; i<=n-diagonal; i++)
        {
            j = i + diagonal;
            for(k=i; k<=j-1; k++)
            {
                if(k==i)
                {
                    M[i][j] = M[i][k]+M[k+1][j]+d[i-1]*d[k]*d[j];
                    P[i][j] = k;
                }
                else if(M[i][j]>M[i][k]+M[k+1][j]+d[i-1]*d[k]*d[j])
                {
                    M[i][j] = M[i][k]+M[k+1][j]+d[i-1]*d[k]*d[j];
                    P[i][j] = k;
                }
            }
        }
    }
    return M[1][n];
}

void order(int i, int j, int**& P)
{
    int k;
    if(i==j) printf("A%d", i);
    else
    {
        k=P[i][j];
        printf("(");
        order(i, k, P);
        order(k+1, j, P);
        printf(")");
    }
}







