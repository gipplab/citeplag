<?php
/**
 * Error handler
 *
 * Provides Error Capturing for Framework errors.
 *
 * PHP 5
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright 2005-2012, Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2005-2012, Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @package       Cake.Error
 * @since         CakePHP(tm) v 0.10.5.1732
 * @license       MIT License (http://www.opensource.org/licenses/mit-license.php)
 */

App::uses('Debugger', 'Utility');
App::uses('CakeLog', 'Log');
App::uses('ExceptionRenderer', 'Error');

/**
 *
 * Error Handler provides basic error and exception handling for your application. It captures and
 * handles all unhandled exceptions and errors. Displays helpful framework errors when debug > 1.
 *
 * ### Uncaught exceptions
 *
 * When debug < 1 a CakeException will render 404 or  500 errors.  If an uncaught exception is thrown
 * and it is a type that ErrorHandler does not know about it will be treated as a 500 error.
 *
 * ### Implementing application specific exception handling
 *
 * You can implement application specific exception handling in one of a few ways.  Each approach
 * gives you different amounts of control over the exception handling process.
 *
 * - Set Configure::write('Exception.handler', 'YourClass::yourMethod');
 * - Create AppController::appError();
 * - Set Configure::write('Exception.renderer', 'YourClass');
 *
 * #### Create your own Exception handler with `Exception.handler`
 *
 * This gives you full control over the exception handling process.  The class you choose should be
 * loaded in your app/Config/bootstrap.php, so its available to handle any exceptions.  You can
 * define the handler as any callback type. Using Exception.handler overrides all other exception
 * handling settings and logic.
 *
 * #### Using `AppController::appError();`
 *
 * This controller method is called instead of the default exception rendering.  It receives the
 * thrown exception as its only argument.  You should implement your error handling in that method.
 * Using AppController::appError(), will supersede any configuration for Exception.renderer.
 *
 * #### Using a custom renderer with `Exception.renderer`
 *
 * If you don't want to take control of the exception handling, but want to change how exceptions are
 * rendered you can use `Exception.renderer` to choose a class to render exception pages.  By default
 * `ExceptionRenderer` is used.  Your custom exception renderer class should be placed in app/Lib/Error.
 *
 * Your custom renderer should expect an exception in its constructor, and implement a render method.
 * Failing to do so will cause additional errors.
 *
 * #### Logging exceptions
 *
 * Using the built-in exception handling, you can log all the exceptions
 * that are dealt with by ErrorHandler by setting `Exception.log` to true in your core.php.
 * Enabling this will log every exception to CakeLog and the configured loggers.
 *
 * ### PHP errors
 *
 * Error handler also provides the built in features for handling php errors (trigger_error).
 * While in debug mode, errors will be output to the screen using debugger.  While in production mode,
 * errors will be logged to CakeLog.  You can control which errors are logged by setting
 * `Error.level` in your core.php.
 *
 * #### Logging errors
 *
 * When ErrorHandler is used for handling errors, you can enable error logging by setting `Error.log` to true.
 * This will log all errors to the configured log handlers.
 *
 * #### Controlling what errors are logged/displayed
 *
 * You can control which errors are logged / displayed by ErrorHandler by setting `Error.level`.  Setting this
 * to one or a combination of a few of the E_* constants will only enable the specified errors.
 *
 * e.g. `Configure::write('Error.level', E_ALL & ~E_NOTICE);`
 *
 * Would enable handling for all non Notice errors.
 *
 * @package       Cake.Error
 * @see ExceptionRenderer for more information on how to customize exception rendering.
 */
class AppExceptionRenderer extends ExceptionRenderer {
    function _outputMessage($template) {
        // Call the "beforeFilter" method so that the "Page Not Found" page will
        // know if the user is logged in or not and, therefore, show the links that
        // it is supposed to show.

        if (Configure::read('App.maintenance') == false)
        {
            $this->controller->beforeFilter();
        }

        parent::_outputMessage($template);
    }

    public function downForMaintenance() {
        $url = $this->controller->request->here();
        $code = 403;
        $this->controller->response->statusCode($code);
        $this->controller->set(array(
            'code' => $code,
            'url' => h($url),
            'isMobile' => $this->controller->RequestHandler->isMobile(),
            'logged_in' => false,
            'title_for_layout' => 'Down for Maintenance'
        ));
        $this->_outputMessage($this->template);
    }
}
