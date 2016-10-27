# Knowledgbase ESE2016

## GIT Branch Model
Try to stay near this [model](http://nvie.com/posts/a-successful-git-branching-model/) from [Vincent Driessen](http://nvie.com/about/).

## GIT
|command|purpose|comment|
|:------|:------|:------|
|```git push -u origin <branchName>:<branchNameOnGit>```|push of a local branch to remote origin. '-u' stands for 'upstream'|origin has to be https://github.com/scg-unibe-ch/ese2016-team4.git, otherwise add it...|
|```git push origin --delete <branch_name>```|delete remote branch||
|```git pull origin <remoteBranch>:<localBranchName>```|pull remote Branch from remote origin|
|```git remote add origin https://github.com/scg-unibe-ch/ese2016-team4.git```|adds origin|check with ```git remote -v``` to which remote youpul/fetch|
|```git remote rm origin <your remote>```|removes origin||
|<div>```git fetch origin```</div> <div>```git reset --hard origin/master```</div>|Setting your branch to exactly match the remote branch||
|```git revert HEAD~3..HEAD```| reverts back the last 3 commits| Beahves as a normal commit and should be prefered over ```revert``` to go back on **public** repos! *If there were merges on the last commits, set the ```-m``` option!*|
|```git revert f9c142..7431e2```|revert all commits between those commit-id's (oldId..newId)||
|```git revert m 1 f9c142```|revert over a branch, where ```1``` means that you revert back to the branch merged to (and ```2``` would mean, you're going back the path of the branch merged from)||
|```for remote in `git branch -r `; do git branch --track $remote; done```|track all remote branches|bash-script|
