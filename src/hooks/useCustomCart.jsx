import { useRecoilState } from "recoil";
import { cartState } from "../atoms/cartState";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getCartItems, postChangeCart } from "../api/CartApi";
import { useEffect } from "react";

const useCustomCart = () => {
  // recoil, reactQuery 쓸거라 밑 코드는 안씀
  // const cartItems = useSelector((state) => state.cartSlice);

  // const dispatch = useDispatch();

  // const refreshCart = () => {
  //   dispatch(getCartItemsAsync());
  // };

  // const changeCart = (param) => {
  //   //
  //   dispatch(postChangeCartAsync(param));
  // };

  // return { cartItems, refreshCart, changeCart };

  const [cartItems, setCartItems] = useRecoilState(cartState);

  // 뮤테이션 하고나선 기존에 있던 카드 데이터는 무효화 해야함
  const queryClient = useQueryClient();

  const changeMutation = useMutation({
    // useMutation 이랑 useQuery, recoil 좀더 공부해보기
    mutationFn: (param) => postChangeCart(param),
    onSuccess: (result) => {
      setCartItems(result);
    },
  });

  // 처음 카트 조회
  const query = useQuery({
    queryKey: ["cart"],
    queryFn: getCartItems,
    staleTime: 1000 * 60 * 60,
  });

  useEffect(() => {
    if (query.isSuccess) {
      queryClient.invalidateQueries("cart");
      setCartItems(query.data);
    }
  }, [query.isSuccess]);

  const changeCart = (param) => {
    changeMutation.mutate(param);
  };

  return { cartItems, changeCart };
};

export default useCustomCart;
