/**
 * Created by Le on 1/11/2016.
 */

var cartApp = angular.module("cartApp", []);

cartApp.controller("cartCtrl", function ($scope, $http) {

    $scope.refreshCart = function () {
        $http.get('/rest/cart/' + $scope.cartId).success(function (data) {
            $scope.cart = data;
        });
    };

    $scope.clearCart = function () {
        $http.get('/rest/cart/clear/' + $scope.cartId).success(function () {
            $scope.refreshCart();
        });
    };

    $scope.initCartId = function (cartId) {
        $scope.cartId = cartId;
        $scope.refreshCart(cartId);
    };

    $scope.addToCart = function (productId) {
        $http.get('/rest/cart/add/' + productId).success(function () {
            alert("Product successfully added to the cart!");
        });
    };
    $scope.removeFromCart = function (productId) {

        $http.get('/rest/cart/remove/' + productId).success(function (data) {
            $scope.refreshCart();
            alert("Product successfully deleted from the cart!");
        });
    };

    $scope.calGrandTotal = function () {
        var grandTotal = 0;

        for (var i = 0; i < $scope.cart.cartItems.length; i++) {
            grandTotal += $scope.cart.cartItems[i].totalPrice;
        }

        return grandTotal;
    };
});