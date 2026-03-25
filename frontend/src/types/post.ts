export type BackendPost = {
  id: number;
  title: string;
  content: string;
  author: string;
  postType: string;
  createdAt: string;
  likes: number;
  comments: number;
  isLikedByCurrentUser?: boolean;
};

export type PostComment = {
  id: number;
  author: string;
  postId: number;
  content: string;
  createdAt: string;
};

export type FeedPostApiItem = {
  id: number;
  title: string;
  content: string;
  author: string;
  postType: string;
  createdAt: string;
  likeCount?: number;
  commentCount?: number;
  likedByCurrentUser?: boolean;
};

export type CursorResponse<T> = {
  data: T[];
  nextCursor: string | null;
  hasNext: boolean;
};

export type CreatePostType = "share" | "announcement" | "achievement";
