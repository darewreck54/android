# Pre-work - Task it!

Task it! is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Derek Hang**

Time spent: **X** hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [] List anything else that you can get done to improve the app functionality!
* [x] Splash Screen!

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/darewreck54/android/blob/master/prework/taskit/gif/taskIt_v2.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

I think the main challenge was understanding the framework, the libraries, and how to use it properly.  

Challenges include:
1) Android has changed a lot over the years and it's hard to determine what are the best practices.  
2) A lot of stackoverflow have old post that aren't relevant to the current android. 
3) Not knowing what the best practices are

Quesiton specific to Todo Task
1) Why use dialog fragments for the edit/view when you could use a fragment?  You could jsut create the project with one activity and multiple fragments that you swap in and out
2) I notice when you pass data from an activity to a dialog new instance, you just pass the arguments you want.  Within the activity dialog constructor, you create a bundle and add the bundle to the fragment argumetns.  On the onCreateView, you extra the info from the fragment.  Why don't you just pass the bundle in the first place into the new instance of the dialog.  Also you could just extract the data and store it as member function of the class.  Seems like there are two ways to do the same thing, but which is better?
3) For a dialogFragment, I used a tool bar.  There is also the option to use a positive/negative button.  What is the perfered way?
4) Also I could have probably coded up the Edit and view dialog frament better since there was duplication of code.  Just didn't have time to organize the code better.
5) The back button is somewhat awkward for the dialog box.  Instead I used an x.  However, i know that you would probably have to include a listener ont he key down or back button of the device to function properly.

## License

    Copyright 2017 Derek Hang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.