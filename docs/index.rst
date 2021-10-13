Welcome to s.RunnerManagers's documentation!
============================================

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Contents:

   installation
   configuration
   support

s.RunnerManager is a wrapper and manager for Github Actions Runners which uses the docker image
from `myoung34 <https://github.com/myoung34/docker-github-actions-runner>`__.

With s.RunnerManager it is possible to automatically create and delete runners as required.
In addition, the runners are reset after each run, which means that the runners are equivalent
to those provided by Github.
