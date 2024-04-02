#include <malloc.h>
#include <iostream>
#include <string>
using namespace std;
#define AREA_INDEX 0
#define PRINT_INDEX 1
#define DRAW_INDEX 2
#define DIMENSION_INDEX 3
#define PI 3.14159

typedef double(*double_method_type)(void *);
typedef void(*void_method_type)(void *);

typedef union{
	double_method_type double_method;
	void_method_type void_method;
}VirtualTableEntry;

typedef VirtualTableEntry * VtableType;

//Shape
struct Shape{
	VtableType VPointer;
	string name;
};

double Shape_area(Shape* _this){
	return 0.0;
}
void Shape_dimensions(Shape* _this){
	cout<<"Shape dimension";
}
void Shape_print(Shape* _this){
	double area = (_this->VPointer[AREA_INDEX]).double_method(_this);
	cout<<_this->name<<"(";
	(_this->VPointer[DIMENSION_INDEX]).void_method(_this);
	cout << ") : ";
	cout << area << endl;
}
void Shape_draw(Shape* _this){
	cout<<"Shape drawing"<<endl;
}


VirtualTableEntry Shape_VTable[]= {
	{.double_method= (double_method_type)Shape_area},
	{.void_method = (void_method_type)Shape_print},
	{.void_method = (void_method_type)Shape_draw},
	{.void_method = (void_method_type)Shape_dimensions}
};
Shape * Shape_shape(Shape* _this, string newName){
	_this->VPointer = Shape_VTable;
	_this->name = newName;
	return _this;
}

//Circle extends Shape
struct Circle {
	VtableType VPointer;
	string name;
	double radius;
};
double Circle_area(Circle* _this){
	return PI * _this->radius * _this->radius;
}
void Circle_dimensions(Circle* _this){
	cout<<_this->radius;
}
void Circle_draw(Circle* _this){
	cout<<"   ****  "<<endl;
	cout<<" *      *"<<endl;
	cout<<"*        *"<<endl;
	cout<<"*        *"<<endl;
	cout<<" *      *"<<endl;
	cout<<"   ****  "<<endl;
}
VirtualTableEntry Circle_VTable[] = {
	{.double_method = (double_method_type)Circle_area}, //override
	{.void_method = (void_method_type)Shape_print}, //inheritance
	{.void_method = (void_method_type)Circle_draw}, //override
	{.void_method = (void_method_type)Circle_dimensions} //override
};

Circle * Circle_circle(Circle* _this, string newName, double newRadius){
	Shape_shape((Shape *)_this, newName);//call parent constructor
	_this->VPointer = Circle_VTable;
	_this->radius = newRadius;
	return _this;
}
//Triangle extends Shape
struct Triangle{
	VtableType VPointer;
	string name;
	double base;
	double height;
};

double Triangle_area(Triangle* _this){
	return (_this->base * _this->height)/2.0;
}
void Triangle_dimensions(Triangle* _this){
	cout<<_this->base<<", "<<_this->height;
}
void Triangle_draw(Triangle* _this){
	cout<<"   *   "<<endl;
	cout<<"  * *  "<<endl;
	cout<<" *   * "<<endl;
	cout<<"*******"<<endl;
}
VirtualTableEntry Triangle_VTable[] = {
	{.double_method = (double_method_type)Triangle_area},
	{.void_method = (void_method_type)Shape_print},
	{.void_method = (void_method_type)Triangle_draw},
	{.void_method = (void_method_type)Triangle_dimensions}
};
Triangle* Triangle_tri(Triangle* _this, string newName, double newBase, double newHeight){
	Shape_shape((Shape*)_this, newName);
	_this->VPointer = Triangle_VTable;
	_this->base= newBase;
	_this->height = newHeight;
	return _this;
}

struct Square{
	VtableType VPointer;
	string name;
	double side;
};
double Square_area(Square* _this){
	return _this->side*_this->side;
}
void Square_dimensions(Square* _this){
	cout<<_this->side;
}
void Square_draw(Square* _this){
	cout<<"*****"<<endl;
	cout<<"*   *"<<endl;
	cout<<"*****"<<endl;
}
VirtualTableEntry Square_VTable[] = {
	{.double_method =(double_method_type)Square_area},
	{.void_method = (void_method_type)Shape_print},
	{.void_method = (void_method_type)Square_draw},
	{.void_method = (void_method_type)Square_dimensions}
};

Square* Square_square(Square* _this, string newName, double newSide){
	Shape_shape((Shape*)_this, newName);
	_this->VPointer = Square_VTable;
	_this->side = newSide;
	return _this;
}
//Rectangle extends Square
struct Rectangle{
	VtableType VPointer;
	string name;
	double side;
	double width;
};

double Rectangle_area(Rectangle* _this){
	return _this->side * _this->width;
}
void Rectangle_dimensions(Rectangle* _this){
	cout<<_this->side <<", "<< _this->width;
}
void Rectangle_draw(Rectangle* _this){
	cout<<"*****"<<endl;
	cout<<"*   *"<<endl;
	cout<<"*   *"<<endl;
	cout<<"*   *"<<endl;
	cout<<"*****"<<endl;
}

VirtualTableEntry Rectangle_VTable[]={
	{.double_method = (double_method_type)Rectangle_area},
	{.void_method = (void_method_type)Shape_print},
	{.void_method = (void_method_type)Rectangle_draw},
	{.void_method = (void_method_type)Rectangle_dimensions}
};

Rectangle* Rectangle_rect(Rectangle* _this, string newName, double newSide, double newWidth){
	Square_square((Square*)_this, newName, newSide);
	_this->VPointer = Rectangle_VTable;
	_this->width = newWidth;
	return _this;
}
void drawAll(Shape* shapes[], int size){
	for(int i=0;i<size;i++){
		shapes[i]->VPointer[DRAW_INDEX].void_method(shapes[i]);
	}
}	
void printAll(Shape* shapes[], int size){
	for(int i=0;i<size;i++){
		shapes[i]->VPointer[PRINT_INDEX].void_method(shapes[i]);
	}
}
double totalArea(Shape* shapes[], int size){
	double total = 0;
	for(int i=0;i<size;i++){
		total+=shapes[i]->VPointer[AREA_INDEX].double_method(shapes[i]);
	}
	return total;
}
int main(int argc, char* argv[]){
	if(argc<=2){
		cout<<"Error of command line\n";
		return 0;
	}
	int arg1 = stod(argv[1]);
	int arg2 = stod(argv[2]);
	Shape* pictures[] = {
		(Shape*)Triangle_tri((Triangle*)malloc(sizeof(Triangle)), "FirstTriangle", arg1, arg2),
		(Shape*)Triangle_tri((Triangle*)malloc(sizeof(Triangle)), "SecondTriangle", arg1-1,arg2-1),
	       	(Shape*)Circle_circle((Circle*)malloc(sizeof(Circle)),"FirstCircle", arg1),
 	        (Shape*)Circle_circle((Circle*)malloc(sizeof(Circle)), "SecondCircle", arg1-1),
		(Shape*)Square_square((Square*)malloc(sizeof(Square)), "FirstSquare", arg1),
		(Shape*)Square_square((Square*)malloc(sizeof(Square)), "SecondSquare", arg1-1),
		(Shape*)Rectangle_rect((Rectangle*)malloc(sizeof(Rectangle)), "FirstRectangle", arg1, arg2),
		(Shape*)Rectangle_rect((Rectangle*)malloc(sizeof(Rectangle)), "SecondRectangle", arg1-1, arg2-1),
	};
	int psize = sizeof(pictures)/sizeof(*pictures);
	printAll(pictures, psize);
	drawAll(pictures, psize);
	cout<<"Total: "<<totalArea(pictures, psize)<<endl;


}


	
	
