#include "vector.h"

int main() // Here is a start:

{

        Vector<int> intVec{1,3,5,7,9};

        Vector<double> doubleVec{1.5,2.5,3.5,4.5};

        Vector<int> iv{intVec};

        Vector<double> dv{doubleVec};

        cout << "intVec" << intVec << endl;

// "intVec(1, 3, 5, 7, 9)"

        cout << "iv" << iv << endl;

// "iv(1, 3, 5, 7, 9)"

        cout << "doubleVec" << doubleVec << endl;

// "doubleVec(1.5, 2.5, 3.5, 4.5)"

cout << "dv" << dv << endl;


// "dv(1.5, 2.5, 3.5, 4.5)"

        //test contructor with size
        Vector<int> iv2(3);
        //test size()
        cout<<"size of iv2: "<<iv2.size()<<endl;
        //test throw
        //iv2[7] = 3;
        //cout<<iv2[7]<<endl;
// "size of iv2: 3"
        //test [] operator change value
        iv2[0] = 3;
        cout<<"iv2[0] = "<<iv2[0]<<endl;
// "iv2[0] = 3"
        //test [] operator access the value
        int num = intVec[2];
        cout<<"num is "<<intVec[2]<<endl;
// "num is 5"
        // test * operator
        Vector<double> doubleVec2{2.3,2.0,1.5,2.7};
        double dotProd = doubleVec*doubleVec2;
        cout<<"docProd is "<<dotProd<<endl;
// "docProd is 25.85"
        //test operator +
        Vector<int> intVec2{1,1,1};
        Vector<int> sum = intVec + intVec2;
        cout<< "sum "<< sum <<endl;
// "sum (2,4,6,7,9)"
        intVec= intVec2;
        cout<< "new intVec is "<<intVec<<endl;
// "new intVec is (1,1,1)"
        //test ==
        if(intVec==intVec2){
                cout<<"intVec is equal to intVec2\n";
        }else{
                cout<<"intVec is not equal to intVec2\n";
        }
// "intVec is equal to intVec2\n"
        //test != 
        if(doubleVec==doubleVec2){
                cout<<"doubleVec is equal to doubleVec2\n";
        }else{
                cout<<"doubleVec is not equal to doubleVec2\n";
        }
// "doubleVec is not equal to doubleVec2"
        //test V1 = 20 * V2
        Vector<int> intVec3 = 20 * intVec;
        cout<< "intVec3 is "<<intVec3<<endl;
// "intVec3 is (20,20,20)"
        //test V1 = 20 + V2
        Vector<double> doubleVec3 = 10 + doubleVec;
        cout<< "doubleVec3 is "<<doubleVec3<<endl;
// "doubleVec3 is (11.5, 12.5, 13.5, 14.5)"
        //int removed = intVec.remove(0);
        //cout<<removed<<endl;

return 0;

}