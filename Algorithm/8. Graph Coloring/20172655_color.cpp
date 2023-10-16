//Graph Coloring
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
#include <algorithm>
using namespace std;
typedef int index;

int n;
int m;
int** W;
int* vcolor;

void m_coloring (index i);
bool promising(index i);

int main()
{
    int input1, input2;
    int cnt=1;
    int check;
    printf("< Graph Coloring >\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("평지의 수(n)를 입력하세요 : ");
    scanf("%d", &n);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("색상의 수(m)를 입력하세요 : ");
    scanf("%d", &m);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    vcolor=(int*)malloc(sizeof(int)*(n+1));
    W=(int**)malloc(sizeof(int*)*(n+1));
    for(int i=0; i<=n; i++)
        W[i]=(int*)malloc(sizeof(int)*(n+1));
    for(int i=1; i<=n; i++)
        for(int j=1; j<=n; j++)
            W[i][j]=false;
    printf("이음선을 입력하세요.\n");
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
    m_coloring(0);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<끝>\n");
    //////////
    free(vcolor);
    for(int i=0; i<=n; i++)
        free(W[i]);
    free(W);
}
void m_coloring(index i)
{
    int color;
    if(promising(i))
    {
        if(i==n)
        {
            for(int j=1; j<=n; j++)
            {
                printf("[v%d : color(%d)] ", j, vcolor[j]);
            }
            printf("\n");
        }
        else
        {
            for(color=1; color<=m; color++)
            {
                vcolor[i+1]=color;
                m_coloring(i+1);
            }
        }
    }
}
bool promising(index i)
{
    index j;
    bool switcher;
    switcher=true;
    j=1;
    while(j<i&&switcher)
    {
        if(W[i][j]&&vcolor[i]==vcolor[j])
            switcher = false;
        j++;
    }
    return switcher;
}
    
    

