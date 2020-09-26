import 'package:flutter/material.dart';
import 'package:flutter_app/model/api_adpter.dart';
import 'package:flutter_app/model/model_quiz.dart';
import 'package:flutter_app/screen/screen_quiz.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';


class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen>{

  List<Quiz> quizs = [];
  bool isLoading = false;

  _fetchQuizs() async {
    setState(() {
      isLoading = true;
    });
    final response = await http.get('https://drf-quiz-test.herokuapp.com/quiz/3/');
    if(response.statusCode == 200){
      setState(() {
        quizs = parseQuizs(utf8.decode(response.bodyBytes));
        isLoading = false;
      });
    }else{
      throw Exception('faild to load database');
    }
  }

  // List<Quiz> quizs = [
  //   Quiz.fromMap({
  //     'title' : 'test',
  //     'candidates' : ['a', 'b', 'c', 'd'],
  //     'answer': 0
  //   }),
  //   Quiz.fromMap({
  //     'title' : 'test',
  //     'candidates' : ['a', 'b', 'c', 'd'],
  //     'answer': 0
  //   }),
  //   Quiz.fromMap({
  //     'title' : 'test',
  //     'candidates' : ['a', 'b', 'c', 'd'],
  //     'answer': 0
  //   }),
  //   Quiz.fromMap({
  //     'title' : 'test',
  //     'candidates' : ['a', 'b', 'c', 'd'],
  //     'answer': 0
  //   }),
  // ];



  @override
  Widget build(BuildContext context){
    Size screenSize = MediaQuery.of(context).size;
    double width = screenSize.width;
    double height = screenSize.height;

    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: Text('My Quiz App'),
          backgroundColor: Colors.deepPurple,
          leading: Container(),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Center(
                child: Image.asset('images/apple.jpg',
              width: width * 0.8,
            ),
            ),
            Padding(
              padding: EdgeInsets.all(width * 0.024),
            ),
            Text("퀴즈 앱 플러터",
            style: TextStyle(
              fontSize: width * 0.065,
              fontWeight: FontWeight.bold,
              ),
            ),
            Text("퀴즈를 풀기 전 안내사항",
            textAlign: TextAlign.center,),
            Padding(
              padding: EdgeInsets.all(width * 0.048),
            ),
            _buildStep(width, '1.랜덤 문제'),
            _buildStep(width, '2.랜덤 문제'),
            _buildStep(width, '3.랜덤 문제'),
            Padding(
              padding: EdgeInsets.all(width * 0.048),
            ),
            Container(padding: EdgeInsets.only(bottom: width * 0.036),
            child: Center(
              child: ButtonTheme(
                minWidth: width * 0.8,
                height: height * 0.05,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10)
                ),
                child: RaisedButton(
                  child: Text(
                    '지금 퀴즈 풀기',
                    style: TextStyle(color: Colors.white),
                  ),
                  color: Colors.deepPurple,
                  onPressed: () {
                    _fetchQuizs().whenComplete(() {
                      return   Navigator.push(context, MaterialPageRoute(
                          builder: (context) => QuizScreen(
                            quizs: quizs,
                          ))
                      );
                    });
                  },
                ),
              ),
            ),
            ),
          ],
        ),
      )
    );
  }


  Widget _buildStep(double width, String title){
    return Container(
      padding: EdgeInsets.fromLTRB(
          width * 0.048,
          width * 0.024,
          width * 0.048,
          width * 0.024
      ),
      child: Row(crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Icon(
          Icons.check_box,
          size: width * 0.04,
        ),
        Padding(
          padding: EdgeInsets.only(right: width * 0.024),
        ),
        Text(title),
      ],),
    );
  }
}