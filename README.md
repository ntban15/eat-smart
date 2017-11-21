# EatSmart
An application for managing diets and searching recipes which meet certain critera. It also features real-time nutritional analysis of food images taken from device camera.

This application is developed in cooperation with a classmate for our school-based mobile development course.

## Demo

[![EatSmart Demo](https://i.imgur.com/F6SEM0P.png)](https://youtu.be/jRNCWaTL2IQ "EatSmart Demo")

## Screenshots

<img src="https://i.imgur.com/2A2uOnU.png" width="150">
<img src="https://i.imgur.com/XWvKiJs.png" width="150">
<img src="https://i.imgur.com/2JYzjPz.png" width="150">
<img src="https://i.imgur.com/yh6wILN.png" width="150">
<img src="https://i.imgur.com/IG1BdOS.png" width="150">
<img src="https://i.imgur.com/gvGgyAV.png" width="150">
<img src="https://i.imgur.com/T58atmW.png" width="150">
<img src="https://i.imgur.com/aQ8BEVP.png" width="150">
<img src="https://i.imgur.com/fymQQBM.png" width="150">

## Features
- Authentication with Facebook to store diets information
- Many diet customizations, including name, diet type, excluded ingredients, calories, fat, carbs and protein.
- Visual analysis of each diet by using progress circles.
- Searching recipes based on diet customizations.
- Details of each recipe, including source website and step-by-step instructions.
- Adding and removing recipes with ease, either from diet management activity or recipes searching activity.
- Many diets for each account. Only one is activated at a given time.
- Real-time nutritional analysis of food images.

## Applied Techniques
- Clean Model-View-Presenter pattern with Dagger for dependency injection.
- RecyclerView with StaggeredGridLayoutManager to obtain the look of Pinterest.
- Using SnapHelper to snap items according to views.
- Implementing OnLongClickListener to enable contextual menu when holding an item in RecyclerView.
- Handling API calls with Retrofit.
- Managing database with Firebase.

## External Libraries, SDKs and APIs
- Android Support Library
- [Butterknife](http://jakewharton.github.io/butterknife/)
- [GreenRobot EventBus](http://greenrobot.org/eventbus/)
- [Glide](https://github.com/bumptech/glide)
- [Retrofit](http://square.github.io/retrofit/)
- [Dagger](https://github.com/google/dagger)
- [GSON](https://github.com/google/gson)
- [Parcerler](https://github.com/johncarl81/parceler)
- [CircleProgressView](https://github.com/jakob-grabner/Circle-Progress-View)
- [Clarifai](https://www.clarifai.com/)
- Facebook SDK
- Firebase
- [Spoonacular](https://spoonacular.com/)

## Install and build
- Clone the repo
- Contact me for gradle.properties (which contains several API keys) and put it in root folder
- Open the project in Android Studio
- Build and run

## Acknowledgements
- Props to [npkhoa2197](https://github.com/npkhoa2197) for his collaboration.
- [This guy](https://enoent.fr/blog/2015/01/18/recyclerview-basics/) for his instructions on RecyclerView basics.
- And [this guy](https://medium.com/codeword/an-in-depth-look-at-snaphelper-new-tools-in-recyclerview-part-2-5a461a53bf4e) for his detailed guide on SnapHelper.