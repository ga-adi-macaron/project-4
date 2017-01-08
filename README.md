## Overview

**XB Media & Market App**
This app is a simple, easy to use app that gives gamers the things that they need right upfront.  The thing that puts this app apart from other apps is that you can download the game clips and screenshots from Xbox live directly onto your phone, no other app gives you that option..even the actual Xbox Smartglass App. This app also gives gamers the information they need to get the full potential out of their Xbox Live Membership by showing the Free games and games on sale specifically for Gold Members.

**Tech/Libraries**, 
In this app I mainly used Async Tasks to do all the API calls to xboxapi.com.  I also used the Facebook SDK to have a sharing feature, and used an android library found from Android Arsenal (fm.jiecao:jiecaovideoplayer:5.1) to make the fullscreen video player with swipe commands for volume change and time scrubbing. For downloading, I just used the native DownloadManager to queue up and download clips/screenshots

**Screenshots**
![Home Page](/Screenshots/homepage.png)


## Technical Requirements

Your app must:

- **Be a complete application** that provides a thorough solution to the problem you choose to address
- **Have an impressive design and user experience** that follows [Google's Material Design Guidelines](https://developer.android.com/design/index.html) and can impress future clients and employers
- **Use at least one API or SDK**
- **Implement thoughtful user stories** that are significant enough to help you know which features to build and which to scrap
- **Be object oriented** with organized, well-documented, [DRY](https://en.wikipedia.org/wiki/Don't_repeat_yourself) code
- **Be robust**: handle orientation changes without losing state, and handle of failure well (e.g., failed network calls) gracefully
- **Be available on the Google Play store**, so it is publicly available


## Code of Conduct

As always, your app must adhere to General Assembly's [student code of conduct guidelines](https://charlesdrews.gitbooks.io/ga-adi-macaron/content/markdown/code-of-conduct.html).

If you have questions about whether or not your work adheres to these guidelines, please speak with a member of your instructional team.


## Timeline and Deliverables

#### Week 10: Brainstorming and Research

- Determine what problem(s) you'd like to address or niche you'd like to fill with your app


- Write a **Research Plan** ([use this format](assets/research-plan-worksheet.pdf)) and add it to a `research.md` markdown file to be handed in with your completed project


- Conduct _at least 3_ **user interviews**


- Add the following to your `research.md` file after your research plan:
  - **Research Highlights** ([use this format](assets/research-highlights-worksheet.pdf))
  - **User Personas** ([use this format](assets/persona-worksheet.pdf))
  - **Problem Statement(s)** ([use this format](assets/problem-statement-worksheet.pdf))
  - **Competitive Analysis** ([feature inventory or +/Δ model](https://github.com/ga-adi-macaron/Course-Materials/tree/master/lessons/product-development/competitive-research-lesson#introduction-competitive-analysis-15-mins); embed in `research.md` as a [screenshot](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#images) or a [markdown table](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#tables))



#### Week 11: Prioritization, Proposal, and Planning

- Use a [**2x2 Matrix**](https://github.com/ga-adi-macaron/Course-Materials/tree/master/lessons/product-development/feature-prioritization#demo-2x2-matrix-feature-prioritization-10-mins) to prioritize your possible features


- Write a **Project Proposal** (either in a `proposal.md` file or a Google Slides / PowerPoint deck) which includes:
  1. **Overall Objectives**, including problems to be solved and user goals to facilitate - these should be _very specific_ and _based on your research_!
  1. **Research Synthesis** - what trends, goals, motivations, and pain points did you identify?
  1. **Target Audience** - must be _very specific_ and represent your interviewees - What do they need and why? How/when/where do they use similar apps? What do they value and what do they avoid? Etc.
  1. **Prioritized list of features** - What problem or user goal does each one address? How did you determine priority?
  1. **Differentiators** from competitors and pain points addressed
  1. **Constraints** you face - e.g. availability of data, need for permissions users may deny, unfamiliar technologies required, etc.
  1. Anything else you think we should know as we decide whether to approve your proposal


- Present your proposal to Drew or Charlie starting at **3:30 pm on Monday, 12/12**
  - [Sign up for a time slot here](https://docs.google.com/spreadsheets/d/1YABj9ZaNxLymnWsgcf2Qew3sGzPqNb0grlpg-DECS-8/edit?usp=sharing)
  - 7 minutes for presentation, 8 minutes for our questions
  - You may need to present a revised proposal on **Tuesday, 12/13** if we are concerned about your initial proposal


- Once your proposal is approved:
  - Create a **Paper Prototype** for all screens you plan to include in your app
  - Write **User Stories** for your features and plan your testing
  - Plan out the classes & interfaces you'll need
  - Sketch out your app's architecture - do you need background services, scheduled jobs, local data storage, etc.? What 3rd party libraries will you use?


#### Week 12: Sprint

Begin building your app. Remember to...

- Commit often & push to GitHub to preserve your progress
- Do your work on separate _feature branches_ and test thoroughly before merging into your master branch - make sure your master branch always works!
- Write tests for your features as you implement them (ideally, even before)
- Choose good, descriptive names for classes & variables and document your code with comments


#### Week 13: Holiday Break

Catch up on sleep! Take some time to review your progress, reevaluate priorities, and get ready to have a productive final week.


#### Week 14: Sprint

Finish implementing and testing your app, then [publish it to the Google Play store](https://developer.android.com/studio/publish/index.html).

Write a `readme.md` file that includes:
- A link to your app on the Google Play store
- What the app does & how it's useful
- [Embedded screenshot(s)](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#images) to show off your features
- Explanation of the technologies, techniques, libraries, etc. you used
- A couple paragraphs about the **general approach you took** in your design process that point to your `research.md` file
- If necessary, **any special instructions on how to build the app in Android Studio**
- Descriptions of any **unsolved problems** or **major hurdles** you had to overcome


#### Week 15: Project Due on Monday, 1/9/17

Submit a pull request to this repo **_by 9:00 am_**. If you created a repo from scratch rather than fork this one, then make a fork, add a link to your other repo, and submit a pull request.

Final deliverables that must be in your repo:
- Source code for a completed, fully-functioning app
- Unit tests and Espresso tests as needed
- Frequent commits dating back to the beginning of the project with useful commit messages
- A `readme.md` file meeting the requirements listed above, including a link to your app on the Google Play store
- Photos of your paper prototypes (or a link to a [POP] prototype)
- Your `proposal.md` file (or link to Google Slides document) meeting the requirements listed above
- Your `research.md` file meeting the requirements listed above

Everyone will present their app to the class starting at 9:00 am. You have a _maximum_ of 10 minutes, with 7 minutes to demo the app and 3 minutes to discuss your process.


## Suggested Ways to Get Started

* **Don’t get too caught up in too many awesome features** – simple is always better. Build something impressive that does one thing well.
* **Design first.** Planning with user stories & wireframes before writing code means you won't get distracted changing your mind – you'll know what to build, and you can spend your time wisely by just building it.
* **Don’t hesitate to write throwaway code** to solve short term problems.
* **Read the docs for whatever technologies / frameworks / API’s you use**.
* **Write DRY code**.
* **Be consistent with your code style.**
* **Commit early, commit often.** Don’t be afraid to break something because you can always go back in time to a previous version.
* **Keep user stories small and well-defined**, and remember – user stories focus on what a user needs, not what development tasks need accomplishing.
* **Write code another developer wouldn't have to ask you about**. Do your naming conventions make sense? Would another developer be able to look at your app and understand what everything is?
* **Make it all well-formatted.** Are you indenting, consistently? Can we find the start and end of every div, curly brace, etc?
* **Comment your code.** Will someone understand what is going on in each block or function? Even if it's obvious, explaining the what & why means someone else can pick it up and get it.
* **Write pseudocode before you write actual code.** Thinking through the logic of something helps.


## Resources

- [Class resources list](https://charlesdrews.gitbooks.io/ga-adi-macaron/content/markdown/resources.html)
- [Android Developer Website](http://developer.android.com/index.html)
- [Google Design Guidelines](https://www.google.com/design/spec/material-design/introduction.html)
- [API Search](http://apis.io) and [ProgrammableWeb](http://www.programmableweb.com/) - find APIs to get the data you need
- [HackDesign](https://hackdesign.org/lessons) and [Visual Design Hacking](https://generalassemb.ly/online/videos/visual-design-hacking) - tips for producing great designs

---

## Licensing
1. All content is licensed under a CC­BY­NC­SA 4.0 license.
2. All software code is licensed under GNU GPLv3. For commercial use or alternative licensing, please contact [legal@ga.co](mailto:legal@ga.co).
