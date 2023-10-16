//The Hamiltonian Circuits Problem
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
#include <algorithm>
using namespace std;
typedef int index;

int n;
int** W;
int* vindex;

void hamiltonian(index i);
bool promising(index i);

int main()
{
    int input1, input2;
    int cnt=1;
    int check;
    printf("< The Hamiltonian Circuits Problem >\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("정점의 개수(n)를 입력하세요 : ");
    scanf("%d", &n);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    vindex=(int*)malloc(sizeof(int)*n);
    W=(int**)malloc(sizeof(int*)*(n+1));
    for(int i=0; i<=n; i++)
        W[i]=(int*)malloc(sizeof(int)*(n+1));
    for(int i=1; i<=n; i++)
        for(int j=1; j<=n; j++)
            W[i][j]=false;
    printf("비방향 그래프 W의 이음선을 입력하세요.\n");
    printf("v1, v3 을 잇는 이음선이 존재할 경우 1 3 으로 입력합니다.\n");
    printf("end 입력 시 입력을 종료합니다.\n");
    while(cnt)
    {
        printf("입력(%d) : ", cnt);
        check=scanf("%d %d", &input1, &input2);
        if(check!=2)
        {
            printf("<입력완료>\n");
            break;
        }
        W[input1][input2]=true;
        W[input2][input1]=true;
        cnt++;
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<< 입력된 그래프의 모든 해밀턴 회로 출력(시작정점 v1) >>\n");
    vindex[0]=1;
    hamiltonian(0);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<끝>\n");
    //////////
    free(vindex);
    for(int i=0; i<=n; i++)
        free(W[i]);
    free(W);
}
void hamiltonian(index i)
{
    index j;
    if(promising(i))
    {
        if(i==n-1)
        {
            printf("[ ");
            for(j=0; j<=n-1; j++)
            {
                printf(" (%d) ->", vindex[j]);
            }
            printf(" (%d) ]\n", vindex[0]);
        }
        else
        {
            for(j=2; j<=n; j++)
            {
                vindex[i+1]=j;
                hamiltonian(i+1);
            }
        }
    }
}
bool promising(index i)
{
    index j;
    bool switcher;
    if(i==n-1 && !W[vindex[n-1]][vindex[0]])
        switcher=false;
    else if(i>0 && !W[vindex[i-1]][vindex[i]])
        switcher=false;
    else
    {
        switcher=true;
        j=1;
        while(j<i&&switcher)
        {
            if(vindex[i]==vindex[j])
                switcher = false;
            j++;
        }
    }
    return switcher;
}
    
    

