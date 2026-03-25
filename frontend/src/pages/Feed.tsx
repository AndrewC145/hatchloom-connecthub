import Post from "../components/Post";
import type { BackendPost } from "../types/post";
import { useState, useEffect } from "react";

// Sample data
const samplePost: BackendPost = {
  id: 1,
  title: "Welcome to ConnectHub!",
  content: "First post test",
  author: "123e4567-e89b-12d3-a456-426614174000",
  postType: "classified",
  createdAt: "2024-06-01T12:00:00Z",
};

function Feed() {
  const [posts, setPosts] = useState<BackendPost[]>([samplePost]);
  return (
    <section className="mx-auto flex flex-col items-center px-4 py-8">
      <h1 className="font-display text-charcoal mb-6 text-2xl font-extrabold">
        Latest Posts
      </h1>
      <div className="flex flex-col items-center space-y-6">
        <Post post={samplePost} />
      </div>
    </section>
  );
}

export default Feed;
