import React, { useEffect } from "react";
import useCustomLogin from "../../hooks/useCustomLogin.jsx";
import { useDispatch, useSelector } from "react-redux";
import { getCartItemsAsync } from "../../slices/cartSlice.jsx";
import useCustomCart from "../../hooks/useCustomCart.jsx";
import CartItemComponent from "../cart/CartItemComponent.jsx";
import { useRecoilValue } from "recoil";
import { cartTotalState } from "../../atoms/cartState.jsx";

const CartComponent = () => {
  const { isLogin, loginState } = useCustomLogin();

  const { cartItems, changeCart } = useCustomCart();

  const totalValue = useRecoilValue(cartTotalState);

  //const dispatch = useDispatch();

  //const cartItems = useSelector((state) => state.cartSlice);

  // const { refreshCart, cartItems, changeCart } = useCustomCart(); // 이 두개 뭔지 좀 봐야할듯

  // useEffect(() => {
  //   if (isLogin) {
  //     //dispatch(getCartItemsAsync());
  //     refreshCart();
  //   }
  // }, [isLogin]);

  return (
    <div className="w-full">
      {/* 로그인 유무에 따라 장바구니 나타내기 */}
      {isLogin ? (
        <div className="flex">
          <div className="m-2 font-extrabold">{loginState.nickname}'s Cart</div>
          <div className="bg-orange-600 w-9 text-center text-white font-bold rounded-full m-2">
            {cartItems.length}
          </div>

          <div>
            <ul>
              {cartItems.map((item) => (
                <li>
                  <CartItemComponent
                    {...item}
                    key={item.cino}
                    changeCart={changeCart}
                    email={loginState.email}
                  />
                </li>
              ))}
            </ul>
          </div>
        </div>
      ) : (
        <div></div>
      )}
    </div>
  );
};

export default CartComponent;
