;;; prj.el --- Project Configuration

;;; Commentary:
;; JDEE Project
;; Author: Zhong Lizhi <zlz.3907 <at> gmail.com>
;; Version: 1.0

;;; Code:
(defvar prj-ant-buildfile "build.xml"
  "Specify a build file name.")
(defvar prj-working-directory "."
  "Specify working directory.")

(defvar prj-ant-buildfile-abspath "."
  "Abslute path of build file.")

(defvar prj-customize-name ""
  "Specify the project name, by default is current directory name.")
(defvar prj-name ""
  "Specify the project name.")
(defvar prj-sourcepath nil
  "Specify source path.")
(defvar prj-classpath nil
  "Specify classpath.")

(setq prj-working-directory
      (file-name-directory (jdee-find-project-file (file-truename "."))))

(setq prj-ant-buildfile-abspath
      (concat prj-working-directory
              prj-ant-buildfile))

(setq prj-name
      (if (eq "" prj-customize-name)
          (file-name-base (directory-file-name prj-working-directory))))


(setq prj-sourcepath
      (list (concat prj-working-directory "src")
            (concat prj-working-directory "test/unit")
            (concat prj-working-directory "test/verify")
            (concat prj-working-directory "test/integration")))

(setq prj-classpath
      (list
       (concat prj-working-directory "lib")
       (concat prj-working-directory "build/main")
       (concat prj-working-directory "build/test")))

(jdee-project-file-version "1.0")
(jdee-set-variables
 '(jdee-project-name prj-name)
 '(jdee-run-working-directory prj-working-directory)
 '(jdee-ant-working-directory prj-working-directory)
 '(jdee-ant-read-target t)
 '(jdee-ant-enable-find t)
 '(jdee-global-classpath prj-classpath)
 '(jdee-compile-option-sourcepath prj-sourcepath)
 '(jdee-compile-option-classpath prj-classpath)
 '(jdee-built-class-path prj-classpath)
 '(jdee-compile-option-directory
   (concat prj-working-directory "build/main"))
 '(jdee-xref-store-prefixes '("org.apache"
                              "com.google"
                              "commons"
                              "junit"
                              "log4j"
                              "dom4j"
                              "jdee" ))
 ;;'(jdee-sourcepath (append prj-global-sourcepath prj-sourcepath))
 )

;;; prj.el ends here
