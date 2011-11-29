<?php

require_once('PHPUnit/Framework/Test.php');
require_once('PHPUnit/Framework/SelfDescribing.php');
require_once('PHPUnit/Framework/TestListener.php');
require_once('PHPUnit/Framework/TestResult.php');
require_once('PHPUnit/Framework/TestSuite.php');

class util_TestRunner {

    function __construct(PHPUnit_Framework_TestSuite $suite) {
        $this->suite = $suite;
    }

    function addFormatter(PHPUnit_Framework_TestListener $formatter) {
        $this->formatter = $formatter;
    }

    function run() {
        $result = new PHPUnit_Framework_TestResult();
        $result->addListener( $this->formatter );
        $this->suite->run($result);
        return $result;
    }
}

?>